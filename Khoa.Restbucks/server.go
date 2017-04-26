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

//node0 := "54.67.63.53:27017"
//node1 := "54.67.118.254:27017""
//node2 := "54.193.42.94:27017"

func main() {

	router := mux.NewRouter()

	oc := controllers.NewOrderController(getSession())

	router.HandleFunc("/starbucks/order", oc.PlaceOrder).Methods("POST")
	router.HandleFunc("/starbucks/order/{id}", oc.GetOrder).Methods("GET")
	router.HandleFunc("/starbucks/order/{id}", oc.CancelOrder).Methods("DELETE")
	router.HandleFunc("/starbucks/order/{id}", oc.UpdateOrder).Methods("PUT")
	router.HandleFunc("/starbucks/order/{id}/pay", oc.PayOrder).Methods("POST")
	fmt.Println("serving on port 9090")
	err := http.ListenAndServe(":9090", router)

	log.Fatal(err)
}

func getSession() (s *mgo.Session) {
	// Connect to our local mongo
	s, _ = mgo.Dial("mongodb://localhost:27017")
	//s, _ = mgo.Dial("mongodb://node0:30001,node1:30002,node2:30003")
	//s, _ = mgo.Dial("mongodb://54.183.172.156:27017,54.153.89.203:27017,54.215.206.250:27017")
	//s, _ = mgo.Dial("mongodb://54.153.89.203:27017")
	return s
}
