# Stateless4j YAML importer

## Purpose

The purpose of this plugin is to generate the code representing a state machine
in the [stateless4j](https://github.com/oxo42/stateless4j) library. As any maven plugin, the user may choose the
phase in which the plugin gets executed. The plugin generates the following classes:
* State: an enum representing all the states contained by the state machine
* Trigger: an enum representing all the triggers that may cause the state machine to change its 
state
* StateMachine: the class in which a `StateMachineConfig<State, Trigger>` (an entity from 
`stateless4j`) is configured, specifying transitions between states, onExit/onEntry methods, and 
so on.
* Delegate: an interface which defines all the onEntry/onExit methods. The `StateMachine` class 
has an instance of this class and it delegates all those methods to it. The user of this plugin 
should provide an implementation of this interface. 

## Usage

The code to import the plugin in a `pom.xml` is something similar to:

```
            <plugin>
                <groupId>com.github.dovaleac</groupId>
                <artifactId>stateless4j-yaml-importer</artifactId>
                <version>1.2.1</version>
                <executions>
                    <execution>
                        <id>execution</id>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <packageName>com.example.generated</packageName>
                    <yamlFileLocation>src/main/resources/stateMachine.yaml</yamlFileLocation>
                    <destinationFolder>src/main/java/com/example/generated</destinationFolder>
                </configuration>
            </plugin>
```

After adding this to the `pom.xml`, in order to generate the files, run:
```
mvn clean compile
```

You can find an example of this at [this repo](https://github.com/dovaleac/stateless4j-yaml-importer-tester)

### Configuration

In order to configure the library, 3 parameters are mandatory:
* `packageName`: the package that will contain the generated classes. Example: 
`com.example.generated`.
* `destinationFolder`: the folder in which the generated files will be.
* `yamlFileLocation`: the location of the yaml file that contains the specification of the state 
machine.

There are other parameters which are optional, because they only refer to internalities of the 
generated code, and should not impact on the user:
* `useTab`: if set to true, tabs will be used. Else (by default) spaces will be used.
* `spacesForTab`: specifies how many spaces are used for each tab. Only if `useTab` is false. 
Default: 2.
* `variableName`: the name of the internal variable used for the configuration. Default: `config`.

### Specifying the state machine

As explained before, the state machine is represented in a yaml file. A good example can be [the 
one used in the tests](src/test/resources/stateMachine.yaml), but here's a more extensive list of
 the parameters:
* `className`: the name of the class that represents the state machine
* `triggerClassName`: the name of the class that represents the triggers
* `delegateInterfaceName`: the name of the class that represents the delegate interface
* `delegateVariableName`: the name of the variable that will represent the delegate in the 
generated code
* `stateMachineParameters`: the list of parameters of the `StateMachine` class. For example, if 
the class was called `StateMachine<T, T2>`, the parameters would be `T` and `T2`.
* `delegateParameters`: the same for the delegate interface.
* `states`: it contains `className` (the name of the State class) and the list of all the states,
 specifying for each of them:
  * `name`
  * `onExit`: list of the methods to be called when leaving this state
  * `onEntry`: list of the methods to be called when entering this state. Must contain the 
  attribute `name` (the name of the method to be called) and optionally the attribute `from`, 
  specifying the trigger that forced the change to this state, in case the user wants the trigger
  to fulfill only if the trigger is the specified one 
  * `superState`: the name of its super state, if any.
* `transitions`: the list of transitions between states. For each transition, the following 
attributes need to be specified:
  * `from`: initial state
  * `to`: new state
  * `trigger`: trigger to be received
* `triggersWithParameters`: in [stateless4j](https://github.com/oxo42/stateless4j), the triggers 
are allowed to have up to 3 parameters. So, for all the triggers that have parameters in the 
state machine, the user needs to specify:
  * `trigger`: the name of the trigger
  * `params`: a list including, for each parameter, its `className`(may be fully qualified, and 
  may be parameterized) and its `variableName`.

### Using the generated code

The user needs to implement the `Delegate` interface and provide an instance of it to the 
`StateMachine` class, like in this example: 

```
    Delegate delegate = new DelegateImpl();

    StateMachineConfig<StateClassName, Trigger> config = new ClassName(delegate).getConfig();

    StateMachine<StateClassName, Trigger> stateMachine = new StateMachine<>(StateClassName.State1
        , config);

    stateMachine.fire(Trigger.FLY);

    stateMachine.fire(new TriggerWithParameters2<>(Trigger.WALK,
        String.class,
        Integer.class), "hi", 0);

    stateMachine.fire(new TriggerWithParameters1<>(Trigger.JUMP,
        Integer.class), 333);

    assertEquals(StateClassName.State4, stateMachine.getState());
```

## Future improvements

* State machine diagrams 
* Providing a method to create the `StateMachine` in 
[stateless4j](https://github.com/oxo42/stateless4j) directly, instead of the `StateMachineConfig`
* Adding utility methods to fire triggers with parameters on a `StateMachine` based on the 
current configuration
