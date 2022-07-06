package co.elastic.apm.opbeans.app.data.source.cart.exceptions

class ProductAlreadyInCartException(id: Int) :
    Exception("Product with id $id is already in the cart")