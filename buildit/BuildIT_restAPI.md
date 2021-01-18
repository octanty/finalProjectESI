## CC7 - View all Submitted POs
GET
http://{{hostBuildit}}/api/procurements/plantHireRequest/viewPO/:id/
use id = 1 until 6

## CC12 - Submit Remittance Advice from BuildIt to Supplier (RentIT)
POST
http://{{hostBuildit}}/api/procurements/remittance/:id/create
use id = 1 or 2

## Info
{{hostBuildit}} = http://localhost:9000/
{{hostRentit}} = http://localhost:8080/
