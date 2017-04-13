package main

import (
	"fmt"
	"log"
	"net/http"
	"./controllers"
	"github.com/gorilla/mux"
	//"encoding/json"
	"gopkg.in/mgo.v2"
)

func main(){

	router := mux.NewRouter()
	
	oc := controllers.NewOrderController(getSession())

	router.HandleFunc("/order",oc.CreateOrder).Methods("POST")
	router.HandleFunc("/order/{id}",oc.GetOrder).Methods("GET")	
	fmt.Println("serving on port 9090")
	err := http.ListenAndServe(":9090",router)
	
	log.Fatal(err)
}

func getSession() (s *mgo.Session) {  
    // Connect to our local mongo
    s, _ = mgo.Dial("mongodb://mongo")
    return s
}