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

	router.HandleFunc("/v1/starbucks/order",oc.PlaceOrder).Methods("POST")
	router.HandleFunc("/v1/starbucks/order/{id}",oc.GetOrder).Methods("GET")	
	router.HandleFunc("/v1/starbucks/order/{id}",oc.CancelOrder).Methods("DELETE")
	router.HandleFunc("/v1/starbucks/order/{id}",oc.UpdateOrder).Methods("PUT")
	//router.HandleFunc("/v1/starbucks/payment/{id}",oc.PayOrder).Methods("PUT")
	fmt.Println("serving on port 9090")
	err := http.ListenAndServe(":9090",router)
	
	log.Fatal(err)
}

func getSession() (s *mgo.Session) {  
    // Connect to our local mongo
    //s, _ = mgo.Dial("mongodb://mongo")
	s, _ = mgo.Dial("mongodb://localhost")
    return s
}