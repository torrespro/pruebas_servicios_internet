package es.urjc.code.daw.library.unitary;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.DockerClientFactory;

public abstract class DockerizedTest {

  @BeforeAll
  public static void shouldRunTest() {
    org.junit.Assume.assumeTrue(isDockerAvailable());
  }

  static boolean isDockerAvailable() {
    try {
      DockerClientFactory.instance().client();
      return true;
    } catch (Throwable ex) {
      return false;
    }
  }

}
