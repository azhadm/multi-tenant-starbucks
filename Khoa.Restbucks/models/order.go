package models

import "gopkg.in/mgo.v2/bson"

type Order struct{
    ID  bson.ObjectId   `json:"id,omitempty" bson:"_id"`
	Location string `json:"location,omitempty" bson:"location"`
    Items []Item   `json:"items,omitempty" bson:"items"`
    Payment Payment `json:"payment,obmitempty" bson:"payment"`
}

type Item struct{
    Quantity int `json:"qty,omitempty" bson:"qty"`
	Name string `json:"name,omitempty" bson:"name"`
    Milk string `json:"milk,omitempty" bson:"milk"`
    Size string `json:"size,omitempty" bson:"size"`
}

type Payment struct{
    Type string `json:"type" bson:"type"`
    Amount float64 `json:"amount" bson:"amount"`
    Card Card `json:"card" bson:"card"`
    Status string `json:"status" bson:"status"`
}

type Card struct{
    Number string `json:"number" bson:"number"`
}