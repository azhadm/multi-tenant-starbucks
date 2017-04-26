package models

import "gopkg.in/mgo.v2/bson"

type Order struct {
	ID       bson.ObjectId     `json:"id"       bson:"_id"`
	Location string            `json:"location" bson:"location"`
	Items    []Item            `json:"items"    bson:"items"`
	Status   string            `json:"status"   bson:"status"`
	Links    map[string]string `json:"links"    bson:"links"`
	Message  string            `json:"message"  bson:"message"`
}

type Item struct {
	Quantity int    `json:"qty"  bson:"qty"`
	Name     string `json:"name" bson:"name"`
	Milk     string `json:"milk" bson:"milk"`
	Size     string `json:"size" bson:"size"`
}
