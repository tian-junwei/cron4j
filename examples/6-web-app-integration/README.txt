This example demonstrates the integration between cron4j and a common web
application.

The example uses a ServletContextListener: 

example.SchedulerServletContextListener

This context listener is used to start and stop the cron4j scheduler at
application startup and shutdown. The listener has been registered in the
/WEB-INF/web.xml descriptor. The listener register a custom task collector in
the scheduler. The collector returns a sample task that has to be executed
every minute. The task counts from 1 to 30.

While the task is running, its executor can be seen and managed with the
example.ExecutionServlet, which is mapped on the /ongoing URI. With this
servlet the user can view any ongoing task execution, and he can also pause,
resume or stop it.

This is a quite good example of what cron4j can do within a server environment.

To run the example on a servlet engine adds the cron4j JAR included in the
software distribution within the /WEB-INF/lib directory (creates it if it
doesn't exist), build the project with your favorite development platform and
deploy it on your favorite servlet engine.
