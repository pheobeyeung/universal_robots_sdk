#include "XMLRPCMethods.hpp"

#include <iostream>

using namespace std;

GetTitle::GetTitle(Data* data) :
    data(data)
{
  this->_signature = "s:"; // RPC method signature, which is not mandatory for basic operation, see http://xmlrpc-c.sourceforge.net/doc/libxmlrpc_server++.html#howto
}

void GetTitle::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value * const retvalP) {
  paramList.verifyEnd(0);
  *retvalP = xmlrpc_c::value_string(data->getTitle());
}

SetTitle::SetTitle(Data* data) :
    data(data)
{
  this->_signature = "s:s";
}

void SetTitle::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value * const retvalP) {
  string const title(paramList.getString(0));
  paramList.verifyEnd(1);

  data->setTitle(title);

  *retvalP = xmlrpc_c::value_string(title); // XML-RPC void return values are an extension to the protocol and not always available or compatible between languages.
}

GetMessage::GetMessage(Data* data) :
    data(data)
{
  this->_signature = "s:s";
}

void GetMessage::execute(xmlrpc_c::paramList const& paramList, xmlrpc_c::value * const retvalP) {
  string const name(paramList.getString(0));
  paramList.verifyEnd(1);

  *retvalP = xmlrpc_c::value_string(data->getMessage(name));
}
