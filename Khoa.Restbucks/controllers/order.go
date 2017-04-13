package controllers

import (
	//"fmt"
	//"log"
	"net/http"
	//"errors"
	"../models"
	"github.com/gorilla/mux"
	"github.com/satori/go.uuid"
	"encoding/json"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
)

type OrderController struct{
	session *mgo.Session
}

func NewOrderController(session *mgo.Session) *OrderController{
	return &OrderController{session}
}

func (oc OrderController) CreateOrder(w http.ResponseWriter, req *http.Request){
	var newOrder models.Order 

	json.NewDecoder(req.Body).Decode(&newOrder)
	

	newOrder.ID = uuid.NewV4().String()

	err := oc.session.DB("test").C("Order").Insert(&newOrder)
	if	err	!=	nil	{
		w.WriteHeader(500)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(201)
	json.NewEncoder(w).Encode(newOrder)
}

func (oc OrderController) GetOrder(w http.ResponseWriter, req *http.Request){

	params := mux.Vars(req)

	result := models.Order{}
	err	:= oc.session.DB("test").C("Order").Find(bson.M{"id":	params["id"]}).One(&result)

	if err != nil {
		//panic(err)
		w.WriteHeader(404)
		return
	}

	w.WriteHeader(200)
	json.NewEncoder(w).Encode(result)
	return
}