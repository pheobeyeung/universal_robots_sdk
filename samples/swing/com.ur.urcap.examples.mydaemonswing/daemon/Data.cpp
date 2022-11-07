#include "Data.hpp"

#include <sstream>

using namespace std;

Data::Data() : title("") {
}

Data::~Data() {
}

std::string Data::getTitle() const{
  string tmp;
  if(title.length() > 0){
    tmp = title;
  } else {
    tmp = "No title set";
  }
  return tmp + " (C++)";
}

void Data::setTitle(std::string title) {
  this->title = title;
}

std::string Data::getMessage(std::string name) const{
  if(name.length() > 0){
    stringstream ss;
    ss << "Hello " << name << ", welcome to PolyScope!";
    return ss.str();
  } else {
    return "No name set";
  }
}

