#pragma once

#include <vector>
#include <xmlrpc-c/registry.hpp>

#include "Data.hpp"

/**
 * @brief xmlrpc-c Abyss server for XML-RPC communication.
 * See: http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server_abyss++.html
 */
class AbyssServer {

  public:
    AbyssServer(Data* data);
    virtual ~AbyssServer();
    static void* run(void* aserver);

  private:
    Data* data;
    xmlrpc_c::registry serviceRegistry;
};

