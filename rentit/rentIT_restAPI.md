# POSTMAN

## Create Purchase Order 
http://{{hostRentit}}/api/sales/orders
{
	"rentalPeriod": {
		"startDate" : "2020-07-07",
		"endDate" : "2020-08-08"
	},
	"plant": {
		"_id": 1,
        "name": "Plant 1",
	    "description": "Description plant 1",
	    "price": 100.00
	}
}


## Accept Purchase Order
http://{{hostRentit}}/api/sales/orders/:id/accept

## Reject Purchase Order
http://{{hostRentit}}/api/sales/orders/:id/reject

## Dispatch Purchase Order
http://{{hostRentit}}/api/sales/orders/:id/dispatch

## Return Purchase Order
http://{{hostRentit}}/api/sales/orders/:id/return

## PS15 - Create and Accept Remittance
POST
http://{{hostRentit}}/api/remittance/:id/create
use id = 1 or 2 (current available invoiceID in database)

POST
http://{{hostRentit}}/api/remittance/:id/accept
use id = 1 or 2 (current available invoiceID in database)
