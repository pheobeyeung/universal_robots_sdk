#include "HelloWorld.hpp"

#include <iostream>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>

#include "AbyssServer.hpp"
#include "Data.hpp"

using namespace std;

bool HelloWorld::quit = false;
int HelloWorld::exit_value = 0;

// called at exit
void HelloWorld::shutdown() {
  cerr << "HelloWorld::shutdown" << endl;
}

// Handle various signals
void HelloWorld::handler(int signum) {

  if(signum == SIGINT) {
    cerr << "HelloWorld::handler caught CTRL-C" << endl;
    HelloWorld::quit = true;
  } else {
    cerr << "HelloWorld::handler caught signal: " << signum << endl;
  }
}

int main(int argc, char *argv[]) {

  // call handler() at CTRL-C (signum = SIGINT)
  if(signal(SIGINT, SIG_IGN) != SIG_IGN) {
    signal(SIGINT, HelloWorld::handler);
  }

  // call shutdown() at program exit
  atexit(HelloWorld::shutdown);

  // Example data container for domain logic
  Data data;

  // To communicate between URScript and the executable we use the xmlrpc-c library
  // This library is standard available on the robot and in the development toolchain.
  AbyssServer gui(&data);

  pthread_t thread_id;
  if(pthread_create(&thread_id, NULL, gui.run, &gui)){
    cerr << "Couldn't create pthread" << endl;
    return EXIT_FAILURE;
  }

  cout << "Hello World started" << endl;

  pthread_join(thread_id, NULL);

  cout << "Hello World stopped" << endl;

  return HelloWorld::exit_value;
}
