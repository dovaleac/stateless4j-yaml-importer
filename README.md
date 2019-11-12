# Stateless4j YAML importer

## Purpose

The purpose of this plugin is to **provide an easy and declarative way (via YAML) to generate 
lightweight code for concurrency-aware state machines in Java**. The 
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

Seems understandable at a first sight? Well, obviously some further explanations on the fields 
will be given, but it's a straightforward way to generate this state machine, with all its 
states, transitions and onEntry/onExit methods:

![Alt text](samples/diagrams/states.png?raw=true "Phone state machine")

## Entities definition

The plugin generates the following classes:
* **State**: an enum representing all the states contained by the state machine
* **Trigger**: an enum representing all the triggers that may cause the state machine to change its 
state
* **StateMachine**: the class in which all the configuration of states, transitions via triggers,
assignment of onExit/onEntry methods to states, and so on, is done. This occurs in a `getConfig
(Delegate)` method which returns a `StateMachineConfig<State, Trigger>` (an entity from 
[stateless4j](https://github.com/oxo42/stateless4j)), but in any case, the user is not intended to 
interact with this class. 
* **Delegate**: an abstract class that the user is intended to implement. It contains:
  * the reference to the `StateMachine` class (so that the user doesn't have to interact with it)
  * the definition of the abstract onExit/onEntry methods, receiving whatever parameters they 
  have. The user needs to implement these methods.
  * utility methods to fire all the triggers. If the trigger X contains params, the method `fireX()`
  will receive those params.

Note: onExit/onEntry methods are simple methods that are called when a state is reached or left. 
They are very important because, in concurrent environments, it's crucial to minimize the number 
of operations that happen out of control. In the [stateless4j](https://github.com/oxo42/stateless4j)
paradigm, the designer of the state machine should try to accomodate _operations that can happen 
at any time_ as triggers that change the state, and use these onExit/onEntry methods to be run in
 a controlled environment.


The user should create the following class:
* **DelegateImpl**: a class that implements the abstract onExit/onEntry methods in the `Delegate` class

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
            <id>moves</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <packageName>com.movements.generated</packageName>
                <yamlFileLocation>src/main/resources/movements.yaml</yamlFileLocation>
                <destinationFolder>samples/src/main/java/com/movements/generated</destinationFolder>
            </configuration>
        </execution>
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
| `useTab` | false | `false` | If true, use tab in generated false. Else use spaces (see next variable) |
| `spacesForTab` | false | `2` | The number of spaces in each tab (if not using tab itself) |
| `variableName` | false | `config` | The name of the config variable (not very useful to change) |
| `generateDiagram` | false | `false` | Specifies whether to generate a state diagram |
| `diagramDestination` | if generating diagram | `diagrams` | The path to the folder where the diagram is to be generated |
| `diagramFileName` | if generating diagram | `states.png` | The name of the diagram file |
| `initialState` | false | `Value` from YAML | The initial state for the diagram |

After adding this to the `pom.xml`, in order to generate the files, run:
```
mvn clean compile
```

### Specifying the state machine in the YAML file

As explained before, the state machine is represented in a yaml file. There are a lot of 
possibilities when configuring the state machine, so here is an extensive list of all of them:

| Variable | Mandatory | Example | Description |
|------------------|-----------|---------|-------------|
| `className` | true | `StateMachine` | the name of the class that represents the state machine |
| `triggerClassName` | true | `Trigger` | the name of the class that represents the triggers |
| `delegateInterfaceName` | true | `AbstractDelegate` | the name of the class that represents the delegate interface |
| `delegateVariableName` | true | `delegate` | the name of the variable that will represent the delegate in thegenerated code |
| `stateMachineParameters[*]` | true | `SMT` | the list of parameters of the `StateMachine` class. For example, if the class was called `StateMachine<T, T2>`, the parameters would be `T` and `T2`. |
| `delegateParameters[*]` | true | `DT` | parameters, the same way, for the delegate interface. |
| `states.className` | true | `State` | the name of the class that represents the states |
| `states.elements[*]` | true | `|` the list of the states, comprising: |
| `states.elements[*]name` | true | `STATE1` | the name of the State class |
| `states.elements[*]onExit[*]` | false | `leaveState1` | list of the methods to be called when leaving this state. |
| `states.elements[*]onEntry[*]` | false | `|` list of the methods to be called when entering this state. |
| `states.elements[*]onEntry[*]name` | true | `enterState1` | name of the method |
| `states.elements[*]onEntry[*]from` | false | `TRIGGER_X` | trigger that initiated the transition to the state |
| `states.elements[*]superState` | false | `STATE0` | the name of its super state, if any. |
| `states.elements[*]initial` | false | `true` | Boolean indicating whether the state is the initial one (for diagrams) |
| `states.elements[*]ignore[*]` | false | `TRIGGER_Y` | List of triggers to ignore when in this state (all the unexpected triggers cause exceptions) |
| `transitions[*]` | true | `|` the list of transitions between states. For each transition, the following attributes need to be specified: |
| `transitions[*]from` | true | `STATE1` | initial state |
| `transitions[*]to` | true | `STATE2` | new state |
| `transitions[*]trigger` | true | `TRIGGER_X` | trigger to be received |
| `triggersWithParameters` | false | `|` in [stateless4j](https://github.com/oxo42/stateless4j), the triggers are allowed to have up to 3 parameters. So, for all the triggers that have parameters in the state machine, the user needs to specify: |
| `triggersWithParameters[*]trigger` | true | `TRIGGER_X` | the name of the trigger |
| `triggersWithParameters[*]params` | true | `|` list of the params for that trigger (maximum, 3)) |
| `triggersWithParameters[*]params.className` | true | `String` | the class of the parameter |
| `triggersWithParameters[*]params.variableName` | true | `stringParam` | the name of the param |
| `eventLog` | false | `|` if this is activated, the specified method will be called in every transition |
| `eventLog.method` | true | `log` | name of the method to be called in every transition |

Notes on the notation used:
* `a[*]` means that `a` is a list of elements
* `a[*]b` means that `a` is a list of elements, and it refers to the `b` field of each of those 
elements
* `c.d` means that `c` is a single element, and it refers to `c`'s `b` field 


### Using the generated code

The user needs to extend the `Delegate` abstract class and provide an implementation of its 
abstract onEntry/onExit/eventLog methods, like 
[here](samples/src/main/java/com/phone/nongenerated/Phone.java) and use it like in this example: 

```
    DelegateImpl delegate = new DelegateImpl(StateClassName.State1);

    delegate.fireFly();
    delegate.fireWalk(new ParameterizedClass("asd", 3.5), 0);

    delegate.fireJump();

    assertEquals(StateClassName.State4, delegate.getCurrentState());
    String expectedHistory = "exit1\n" +
        "exit31\n" +
        "exit32\n" +
        "entry22 asd 3.5 0\n";
    assertEquals(expectedHistory, delegate.currentHistory());
```

This code:
* creates a `Delegate` implementation
* makes use of the _provided-by-the-plugin_ `fireTrigger()` methods
* tests its correctness

## Samples

For an example of most of the features of the yaml, you can have [the 
one used in the tests](stateless-yaml-importer/src/test/resources/stateMachine.yaml).

In the samples folder, you have several examples, specially the one with phones. Take a look at [the 
tests](sample/src/test/java/com/phone/nongenerated/PhoneTest.java)