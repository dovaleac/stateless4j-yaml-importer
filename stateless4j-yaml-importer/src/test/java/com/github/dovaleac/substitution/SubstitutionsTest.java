package com.github.dovaleac.substitution;

import com.github.dovaleac.FileToGenerate;
import com.github.dovaleac.domain.ExecutionConfig;
import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.substitution.Substitutions;
import com.github.dovaleac.substitution.VariableSubstitutionService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SubstitutionsTest {

  private StateMachine stateMachine;
  private final String packageName = "com.github.dovaleac";

  @Before
  public void setUp() throws Exception {
    stateMachine = JacksonService.parseYamlFile(new File("src/test/resources/stateMachine.yaml"));
    ExecutionConfig options = new ExecutionConfig(packageName);
    Substitutions.init(stateMachine, options);
  }

  @Test
  public void state() throws Exception {

    String template = FileToGenerate.STATE.getTemplate();
    String actual = VariableSubstitutionService.get()
        .replaceAll(template);

    String expected =
        "package com.github.dovaleac;\n"
            + "\n"
            + "public enum StateClassName {\n"
            + "  State1, State1son, State2, State3, State4\n"
            + "}";

    assertEquals(expected, actual);
  }

  @Test
  public void trigger() throws Exception {

    String template = FileToGenerate.TRIGGER.getTemplate();
    String actual = VariableSubstitutionService.get()
        .replaceAll(template);

    String expected =
        "package com.github.dovaleac;\n"
            + "\n"
            + "public enum Trigger {\n"
            + "  FALL,\n"
            + "  FLY,\n"
            + "  JUMP,\n"
            + "  WALK\n"
            + "}";

    assertEquals(expected, actual);
  }


  @Test
  public void delegate() throws Exception {

    String template = FileToGenerate.DELEGATE.getTemplate();
    String actual = VariableSubstitutionService.get()
        .replaceAll(template);

    String expected =
        "package com.github.dovaleac;\n"
            + "\n"
            + "import com.github.oxo42.stateless4j.StateMachine;\n"
            + "import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;\n"
            + "import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;\n"
            + "\n"
            + "public abstract class Delegate<T> {\n"
            + "\n"
            + "  protected final StateMachine<StateClassName, Trigger> stateMachine;\n"
            + "\n"
            + "  public Delegate(StateClassName initialState) {\n"
            + "    this.stateMachine = new StateMachine<>(initialState, new ClassName().getConfig(this));\n"
            + "  }\n"
            + "\n"
            + "  public abstract void entry1();\n"
            + "\n"
            + "  public abstract void exit31();\n"
            + "\n"
            + "  public abstract void exit32();\n"
            + "\n"
            + "  public abstract void exit1();\n"
            + "\n"
            + "  public abstract void entry21(Integer height);\n"
            + "\n"
            + "  public abstract void entry22(String param1, Integer param2);\n"
            + "\n"
            + "  public abstract void logTriggerName(Trigger trigger, StateClassName state);\n"
            + "\n"
            + "  public void fireFly() {\n"
            + "    this.stateMachine.fire(Trigger.FLY);\n"
            + "  }\n"
            + "\n"
            + "  public void fireJump() {\n"
            + "    this.stateMachine.fire(Trigger.JUMP);\n"
            + "  }\n"
            + "\n"
            + "  public void fireFall(Integer height) {\n"
            + "    this.stateMachine.fire(new TriggerWithParameters1<>(\n"
            + "      Trigger.FALL, Integer.class),\n"
            + "      height);\n"
            + "  }\n"
            + "\n"
            + "  public void fireWalk(String param1, Integer param2) {\n"
            + "    this.stateMachine.fire(new TriggerWithParameters2<>(\n"
            + "      Trigger.WALK, String.class, Integer.class),\n"
            + "      param1, param2);\n"
            + "  }\n"
            + "\n"
            + "\n"
            + "}";

    assertEquals(expected, actual);
  }

}