# Getting Started

### Swagger API Reference
1. Run the server on localhost
2. Hit http://localhost:8080/swagger-ui/index.html

Sample request
```{
  "billNumber": 0,
  "userId": "4",
  "billItemList": [
    {
      "item": {
        "itemType": "GROCERY",
        "name": "string",
        "amount": 300
      },
      "quantity": 3
    },
{
      "item": {
        "itemType": "BEVERAGE",
        "name": "COKE",
        "amount": 300
      },
      "quantity": 3
    }
  ]
}
```
Sample Response
```{
  "billNumber": 0,
  "initialAmount": 1800,
  "discountList": [
    {
      "discountType": "MULTIPLE_OF_100",
      "amountOfDiscount": 90
    }
  ],
  "totalDiscount": 90,
  "netPayable": 1710
}
```


