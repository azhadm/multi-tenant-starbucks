package models

type Order struct{
    ID  string   `json:"id,omitempty"`
	Location string `json:"location,omitempty"`
    Items []Item   `json:"items,omitempty"`
}

type Item struct{
	
    Quantity int `json:"qty,omitempty"`
	Name string `json:"name,omitempty"`
    Milk string `json:"milk,omitempty"`
    Size string `json:"size,omitempty"`
}