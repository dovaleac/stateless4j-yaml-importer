# Stateless4j YAML importer

## Purpose

The purpose of this plugin is to provide an easy and declarative way **(via YAML)** to generate 
**lightweight concurrency-aware state machines in Java**. The 
[stateless4j](https://github.com/oxo42/stateless4j) library allows
to create this code, but the resulting code is difficult to maintain, because it _tends to be 
spaghetti code_.
 
This plugin provides a way to generate [stateless4j](https://github.com/oxo42/stateless4j) code 
from a YAML file. Also, **the plugin may be used for generating UML state diagrams by reading the 
file**.

As with any maven plugin, the user may choose the phase in which the plugin gets executed. The 
idea, obviously, is not to maintain the generated spaghetti code, but to maintain the more readable 
YAML file and generate the code every time.

## Quick overview

In order to show the power of this plugin, let's just see a YAML file and a diagram (also 
generated via the plugin) showing the lightweight concurrency-aware state machine that it generates.

```
className: PhoneStateMachine
triggerClassName: Trigger
delegateInterfaceName: AbstractPhone
delegateVariableName: phone
transitions:
  - from: IDLE
    to: CALL_ENTERING
    trigger: ReceiveCall
  - from: CALL_ENTERING
    to: RINGING
    trigger: CallEntered
  - from: RINGING
    to: IDLE
    trigger: RejectCall
  - from: RINGING
    to: RINGING
    trigger: ReceiveCall
  - from: RINGING
    to: SPEAKING
    trigger: AcceptCall
  - from: SPEAKING
    to: IDLE
    trigger: FinishCall
  - from: IDLE
    to: CALLING
    trigger: Call
  - from: CALLING
    to: SPEAKING
    trigger: CallAccepted
  - from: SPEAKING
    to: SPEAKING
    trigger: ReceiveCall
  - from: CALLING
    to: IDLE
    trigger: CallRejected
  - from: CALLING
    to: IDLE
    trigger: ReceiverIsBusy
triggersWithParameters:
  - trigger: Call
    params:
      - className: AbstractPhone
        variableName: receiver
  - trigger: AcceptCall
    params:
      - className: AbstractPhone
        variableName: caller
  - trigger: RejectCall
    params:
      - className: AbstractPhone
        variableName: caller
  - trigger: ReceiveCall
    params:
      - className: AbstractPhone
        variableName: caller
  - trigger: CallEntered
    params:
      - className: AbstractPhone
        variableName: caller
states:
  className: State
  elements:
    - name: IDLE
      onEntry:
        - name: notifyCallIsRejected
          from: RejectCall
      initial: true
    - name: CALL_ENTERING
      onEntry:
        - name: startRinging
          from: ReceiveCall
    - name: RINGING
      onEntry:
        - name: shouldICatchIt
          from: CallEntered
        - name: notifyReceiverIsBusy
          from: ReceiveCall
    - name: CALLING
      onEntry:
        - name: callReceiver
    - name: SPEAKING
      onEntry:
        - name: notifyCallIsAccepted
          from: AcceptCall
        - name: notifyReceiverIsBusy
          from: ReceiveCall
eventLog:
  method: log
```

Seems understandable at a first sight? **Some class names, some transitions, some states and we're 
done!** Is that so? Well, obviously some further explanations on the fields 
will be given, but it's a straightforward way to generate this state machine, with all its 
states, transitions and onEntry/onExit methods:

![If not loaded, download the code and open samples/diagrams/states.png](samples/diagrams/states.png?raw=true)

## Entities definition

The plugin generates the following classes:
* **State**: an enum representing all the states contained by the state machine
* **Trigger**: an enum representing all the triggers that may cause the state machine to change its 
state
* **StateMachine**: the class in which all the configuration of states, transitions via triggers,
assignment of onEntry/onExit methods to states, and so on, is done. This occurs in a `getConfig
(Delegate)` method which returns a `StateMachineConfig<State, Trigger>` (an entity from 
[stateless4j](https://github.com/oxo42/stateless4j)), but in any case, the user is not intended to 
interact with this class. 
* **Delegate**: an abstract class that the user is intended to implement. It contains:
  * the reference to the `StateMachine` class (so that the user doesn't have to interact with it)
  * the definition of the abstract onEntry/onExit methods, receiving whatever parameters they 
  have. The user needs to implement these methods.
  * utility methods to fire all the triggers. If the trigger `TRIGGER_X` contains params, the 
  method `fireTriggerX()` will receive those params.

Note: onEntry/onExit methods are simple methods that are called when a state is reached or left. 
They are very important because, in concurrent environments, it's crucial to minimize the number 
of operations that happen _out of control_. 
In the [stateless4j](https://github.com/oxo42/stateless4j)
paradigm, the designer of the state machine should try to accomodate _operations that can happen 
at any time_ as triggers that change the state, and use these onEntry/onExit methods to be run in
 a controlled environment.


The user should create the following class:
* **DelegateImpl**: a class that implements the abstract onEntry/onExit methods in the `Delegate` class

## Usage

### Declaring the plugin in the POM
The code to import the plugin in a `pom.xml` is something similar to:

```
<plugin>
    <groupId>io.github.dovaleac</groupId>
    <artifactId>stateless4j-yaml-importer</artifactId>
    <version>${importer.version}</version>
    <executions>
        <execution>
            <id>phones</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <packageName>com.phone.generated</packageName>
                <yamlFileLocation>src/main/resources/phones.yaml</yamlFileLocation>
                <destinationFolder>samples/src/main/java/com/phone/generated</destinationFolder>
                <generateDiagram>true</generateDiagram>
                <diagramDestination>samples/diagrams/</diagramDestination>
            </configuration>
        </execution>
    </executions>
</plugin>
```

The variables to include in the `configuration` tag are:

| Variable | Mandatory | Default | Description |
|------------------|-----------|---------|-------------|
| `packageName` | true |  | The package for the generated classes |
| `yamlFileLocation` | true |  | The path to the YAML file |
| `destinationFolder` | true |  | The path to the folder where the classes are to be generated |
| `useTab` | false | `false` | If true, use tab in generated files. Else use spaces (see next variable) |
| `spacesForTab` | false | `2` | The number of spaces in each tab (if not using tab itself) |
| `variableName` | false | `config` | The name of the config variable (not very useful to change) |
| `generateDiagram` | false | `false` | Specifies whether to generate a state diagram |
| `diagramDestination` | if generating diagram | `diagrams` | The path to the folder where the diagram is to be generated |
| `diagramFileName` | if generating diagram | `states.png` | The name of the diagram file |
| `initialState` | false | Value from YAML | The initial state for the diagram |

After adding this to the `pom.xml`, in order to generate the files, run:
```
mvn clean compile
```

### Specifying the state machine in the YAML file

As explained before, the state machine is represented in a yaml file. There are a lot of 
possibilities when configuring the state machine, so here is an extensive list of all of them:

| Variable | Mandatory | Example | Description |
|------------------|-----------|---------|-------------|
| `className` | true | `StateMachine` | The name of the class that represents the state machine |
| `triggerClassName` | true | `Trigger` | The name of the class that represents the triggers |
| `delegateInterfaceName` | true | `AbstractDelegate` | The name of the class that represents the delegate interface |
| `delegateVariableName` | true | `delegate` | The name of the variable that will represent the delegate in the generated code |
| `stateMachineParameters[*]` | true | `StateMachineT` | The list of parameters of the `StateMachine` class. For example, if the class was called `StateMachine<T, T2>`, the parameters would be `T` and `T2`. |
| `delegateParameters[*]` | true | `DelegateT` | The list of parameters, the same way, for the delegate interface. |
| `states.className` | true | `State` | The name of the class that represents the states |
| `states.elements[*]` | true | | The list of the states, comprising: |
| `states.elements[*]name` | true | `STATE1` | The name of that state |
| `states.elements[*]onExit[*]` | false | `leaveState1` | The list of the methods to be called when leaving this state. |
| `states.elements[*]onEntry[*]` | false | | The list of the methods to be called when entering this state. |
| `states.elements[*]onEntry[*]name` | true | `enterState1` | The name of the method |
| `states.elements[*]onEntry[*]from` | false | `TRIGGER_X` | The trigger that initiated the transition to the state |
| `states.elements[*]superState` | false | `STATE0` | The name of its super state, if any. |
| `states.elements[*]initial` | false | `true` | A boolean indicating whether the state is the initial one (for diagrams) |
| `states.elements[*]ignore[*]` | false | `TRIGGER_Y` | The list of triggers to ignore when in this state (all the unexpected triggers cause exceptions in [stateless4j](https://github.com/oxo42/stateless4j)) |
| `transitions[*]` | true | | The list of transitions between states. For each transition, the following attributes need to be specified: |
| `transitions[*]from` | true | `STATE1` | The initial state for the transition |
| `transitions[*]to` | true | `STATE2` | The new state for the transition |
| `transitions[*]trigger` | true | `TRIGGER_X` | The trigger that causes the transition |
| `triggersWithParameters` | false | | In [stateless4j](https://github.com/oxo42/stateless4j), the triggers are allowed to have up to 3 parameters. So, for all the triggers that have parameters in the state machine, the user needs to specify: |
| `triggersWithParameters[*]trigger` | true | `TRIGGER_X` | The name of the trigger |
| `triggersWithParameters[*]params` | true | | The list of the params for that trigger (maximum 3; if 0 then the trigger wouldn't show up on this list, which is for triggers that have parameters) |
| `triggersWithParameters[*]params.className` | true | `String` | The class of the parameter. It may be qualified and it may be parameterized |
| `triggersWithParameters[*]params.variableName` | true | `stringParam` | The name of the param |
| `eventLog` | false | | If this is activated, the specified method will be called in every transition no matter what state the machine has or what trigger has been received |
| `eventLog.method` | true | `log` | Name of the method to be called in every transition |

Notes on the notation used:
* `a` means that `a` is a single element
* `a[*]` means that `a` is a list of elements. If there's no `a[*]b` element underneath, then `a` is a list of strings
* `a[*]b` means that `a` is a list of elements, and it refers to the `b` field of each of those 
elements
* `c.d` means that `c` is a single element, and it refers to `c`'s `b` field 


### Using the generated code

The user needs to extend the `Delegate` abstract class and provide an implementation of its 
abstract onEntry/onExit/eventLog methods, like 
[here](samples/src/main/java/com/phone/nongenerated/Phone.java) and use it like in this example: 

```
public static final String MICHAEL = "michael";
public static final String ANNA = "anna";

Phone michael = new Phone(State.IDLE, false, MICHAEL);
Phone anna = new Phone(State.IDLE, false, ANNA);

michael.fireCall(anna);

List<PhoneEvent> expected = List.of(
    new PhoneEvent(MICHAEL, "Call", "CALLING"),
    new PhoneEvent(ANNA, "ReceiveCall", "CALL_ENTERING"),
    new PhoneEvent(ANNA, "CallEntered", "RINGING"),
    new PhoneEvent(ANNA, "AcceptCall", "SPEAKING"),
    new PhoneEvent(MICHAEL, "CallAccepted", "SPEAKING")
);
assertIterableEquals(expected, EventLog.getPhoneEvents());
```

This code:
* creates two `Phone` implementations (in the case of this plugin, it's advisable to contravene the 
classic _use the parent class and not the implementation_ law, because the parent class has been 
generated to make it easier for the implementor of the subclass)
* makes use of the _provided-by-the-plugin_ `fireCall()` method
* tests the events produced as a result

## Samples

For an example of most of the features of the yaml, you can have [the 
one used in the tests](stateless-yaml-importer/src/test/resources/stateMachine.yaml).

In the samples folder, you have several examples, specially the one with phones. Take a look at [the 
tests](sample/src/test/java/com/phone/nongenerated/PhoneTest.java)