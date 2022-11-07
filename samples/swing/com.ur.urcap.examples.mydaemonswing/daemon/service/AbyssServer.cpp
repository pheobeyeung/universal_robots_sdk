#include "AbyssServer.hpp"

#include <memory>
#ifndef PSTREAM
#include <xmlrpc-c/server_abyss.hpp>
#endif

#include "XMLRPCMethods.hpp"

using namespace std;

// Please use a unique port number!
#define SERVER_PORT 40405

AbyssServer::AbyssServer(Data* data) :
    data(data)
{
  // Add all adapters defined in XMLRPCMethods here
  serviceRegistry.addMethod("get_title", new GetTitle(data));
  serviceRegistry.addMethod("set_title", new SetTitle(data));
  serviceRegistry.addMethod("get_message", new GetMessage(data));
}

AbyssServer::~AbyssServer() {
}

void* AbyssServer::run(void* aserver) {

  AbyssServer* ptr = static_cast<AbyssServer*>(aserver);

  xmlrpc_c::serverAbyss server(
      xmlrpc_c::serverAbyss::constrOpt().
      portNumber(SERVER_PORT).
      registryP(&(ptr->serviceRegistry)).
      keepaliveMaxConn(UINT_MAX)
    );

  server.run();

  return NULL;
}
