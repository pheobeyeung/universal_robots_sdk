#pragma once

class HelloWorld {

  public:
    static void shutdown();
    static void handler(int signum);

  public:
    static bool quit; // set to true to shutdown controller
    static int exit_value; // set to a value != 0 to indicate an exit with an error

};
