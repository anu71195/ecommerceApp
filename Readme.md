# Raunak Garment's ecommerce app for shopping.

## Table of contents
  
1. ### [Client flows](##Client-flows)
   1. #### Client flow without interruptions
   1. #### User order flow 
   1. ####  Empty cart flow
   1. #### Profile fields
   1. #### Profile fields error flow
   1. #### Client flow for settings
   1. #### Client flow for contact us screen
2. ### Admin flows
   1. #### Admin navigation flow
   1. #### Add product flow
   1. #### Edit product flow
   1. #### Delete product flow
   1. #### Orders flow
   1. #### Functions flow
   1. #### Developer flow

##Client flows  

### Client flow without interruptions  
 
 <table>
   <tr>
        <td style="text-align: center;"width=30%;">Login screen</td>
        <td style="text-align: center;"width=30%;">Home screen</td>
        <td style="text-align: center;"width=30%;">Logout option</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/normalFlow/loginScreen.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/normalFlow/home.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/normalFlow/logout.PNG" width="240"></td>
   </tr>
 </table>

 <table>
   <tr>
        <td style="text-align: center;"width=30%;">Switch account popup on logout</td>
        <td style="text-align: center;"width=30%;">Navigation bar by clicking on stack structure on top left</td>
        <td style="text-align: center;"width=30%;">Search product</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/normalFlow/logoutPopup.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/normalFlow/NavigationBar.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/normalFlow/search.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Click on product from home screen</td>
        <td style="text-align: center;"width=30%;">Cart screen</td>
        <td style="text-align: center;"width=30%;">Confirm screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/normalFlow/productMagnified.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/normalFlow/cart.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/normalFlow/cartConfirm.PNG" width="240"></td>
   </tr>
 </table>
 
 <table>
    <tr>
         <td style="text-align: center;"width=30%;">Pay with razorpay screen</td>
         <td style="text-align: center;"width=30%;">Razorpay API window</td>
    </tr>
    <tr>
         <td><img style="text-align: center;" src="images/normalFlow/payment.PNG" width="240"></td>
         <td><img style="text-align: center;" src="images/normalFlow/paymentgateway.PNG" width="240"></td>
    </tr>
  </table> 


### User order flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Home screen</td>
        <td style="text-align: center;"width=30%;">Product 1</td>
        <td style="text-align: center;"width=30%;">Product 2</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/userOrdersFlow/home.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/product1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/product2.PNG" width="240"></td>
   </tr>
 </table>
 

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Cart 1</td>
        <td style="text-align: center;"width=30%;">Cart 2</td>
        <td style="text-align: center;"width=30%;">Cart 3</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/userOrdersFlow/cart1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/cart2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/cart3.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Confirm order 1</td>
        <td style="text-align: center;"width=30%;">Confirm order 2</td>
        <td style="text-align: center;"width=30%;">Pay with razorpay screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/userOrdersFlow/confirm1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/confirm2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/payment.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Razorpay API window</td>
        <td style="text-align: center;"width=30%;">Pay with razorpay screen after successful payment</td>
        <td style="text-align: center;"width=30%;">Orders' list screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/userOrdersFlow/paymentGateway.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/successfulPayment.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/myOrders.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Orders' list screen on click of description of one of the orders</td>
        <td style="text-align: center;"width=30%;">Orders' list screen on again click of description of one of the orders</td>
        <td style="text-align: center;"width=30%;">Order summary screen after clicking on button with date on previous screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/userOrdersFlow/myOrdersClick1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/myOrdersClick2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/userOrdersFlow/orderSummary.PNG" width="240"></td>
   </tr>
 </table>

###  Empty cart flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Cart when no item is added in it</td>
        <td style="text-align: center;"width=30%;">Trying to checkout when no item is added</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/emptyCartFlow/emptyCart.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/emptyCartFlow/emptyCartPopup.PNG" width="240"></td>
   </tr>
 </table>
  
### Profile fields  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Profile screen to show the fields</td>
        <td style="text-align: center;"width=30%;">Profile screen with all fields complete and validated</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/workingProfileFlow/profileFields.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/workingProfileFlow/profileComplete.PNG" width="240"></td>
   </tr>
 </table>

### Profile fields error flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Profile Screen opened for first time</td>
        <td style="text-align: center;"width=30%;">Trying to checkout with profile shown in previous screen</td>
        <td style="text-align: center;"width=30%;">Profile screen with updated address</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/errorProfileFlow/profileNew.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/errorProfileFlow/profileCheckoutError.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/errorProfileFlow/profileNewAddress.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Trying to checkout with profile shown in previous screen i.e. with address updated</td>
        <td style="text-align: center;"width=30%;">Profile screen with updated pincode as well</td>
        <td style="text-align: center;"width=30%;">Trying to checkout with profile shown in previous screen i.e. with pincode updated as well</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/errorProfileFlow/profileCheckoutNotAddressError.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/errorProfileFlow/profileNewPinCode.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/errorProfileFlow/profileCheckoutNotPinCodeError.PNG" width="240"></td>
   </tr>
 </table>

### Client flow for settings  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Settings screen all options off (default screen)</td>
        <td style="text-align: center;"width=30%;">Settings screen all options on</td>
        <td style="text-align: center;"width=30%;">Home screen with all options on settings on i.e. it shows items which can't be bought now</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/settingsFlow/settingsOff.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/settingsOn.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/settingsOnResult.PNG" width="240"></td>
   </tr>
 </table>
 
 <table>
    <tr>
         <td style="text-align: center;"width=30%;">Home screen with all options on settings on 2</td>
         <td style="text-align: center;"width=30%;">Product under Maintenance added to cart</td>
         <td style="text-align: center;"width=30%;">Home screen with all options on settings on 3</td>
    </tr>
    <tr>
         <td><img style="text-align: center;" src="images/settingsFlow/settingsOnHome1.PNG" width="240"></td>
         <td><img style="text-align: center;" src="images/settingsFlow/addUnderMaintenanceProduct.PNG" width="240"></td>
         <td><img style="text-align: center;" src="images/settingsFlow/settingsOnHome2.PNG" width="240"></td>
    </tr>
  </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Product not available added to cart</td>
        <td style="text-align: center;"width=30%;">Home screen with all options on settings on 4</td>
        <td style="text-align: center;"width=30%;">Available product added to cart</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/settingsFlow/addNotAvailableProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/settingsOnHome3.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/addAvailableProduct.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Cart 1</td>
        <td style="text-align: center;"width=30%;">Cart 2</td>
        <td style="text-align: center;"width=30%;">Cart 3</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/settingsFlow/cart1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/cart2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/cart3.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Cart 4</td>
        <td style="text-align: center;"width=30%;">Confirm order 1 with underMaintenace product can't be bought</td>
        <td style="text-align: center;"width=30%;">Confirm order 2 with non available product can't be bought</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/settingsFlow/cart4.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/confirmOrder1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/confirmOrder2.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Confirm order 3</td>
        <td style="text-align: center;"width=30%;">Confirm order 4</td>
        <td style="text-align: center;"width=30%;">Pay with razorpay screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/settingsFlow/confirmOrder3.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/confirmOrder4.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/settingsFlow/payment.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Razorpay API window</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/settingsFlow/paymentGateway.PNG" width="240"></td>
   </tr>
 </table>
 

### Client flow for contact us screen  
<table>
   <tr>
        <td style="text-align: center;"width=30%;">Contact us screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/contactUsFlow/contactUs.PNG" width="240"></td>
   </tr>
 </table>

## Admin flows  

### Admin navigation flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Navigation bar on user flow to acess admin flow</td>
        <td style="text-align: center;"width=30%;">Add product screen</td>
        <td style="text-align: center;"width=30%;">Navigation bar on admin flow</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/adminNavigationFlow/adminNavigation.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/adminNavigationFlow/addProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/adminNavigationFlow/adminNavigationInside.PNG" width="240"></td>
   </tr>
 </table>

### Add product flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Add product screen</td>
        <td style="text-align: center;"width=30%;">Admin home screen 1</td>
        <td style="text-align: center;"width=30%;">Admin home screen 2</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/addProductFlow/addProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/allProducts.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/allProductsEnd.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Empty add product screen</td>
        <td style="text-align: center;"width=30%;">Partially filled fields in add product screen</td>
        <td style="text-align: center;"width=30%;">Window displayed on click of upload image button and selecting the shown image</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/addProductFlow/backToAddProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/partiallyFilledAddProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/selectImage.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Image uploaded to firebase and url is created as shown and image is displayed below</td>
        <td style="text-align: center;"width=30%;">Clicking on add product, product is added and screen is reset to empty fields</td>
        <td style="text-align: center;"width=30%;">Admin home screen with new added product</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/addProductFlow/imageAdded.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/productAdded.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/newProductInAdmin1.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicking on new added product from admin home screen</td>
        <td style="text-align: center;"width=30%;">Searching the new added product in admin home screen</td>
        <td style="text-align: center;"width=30%;">Searching the new added product in user home screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/addProductFlow/newProductInAdmin2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/searchAdmin.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/addProductFlow/newProductUser1.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicking on new added product from user home screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/addProductFlow/newProductUser2.PNG" width="240"></td>
   </tr>
 </table>

### Edit product flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Admin home screen</td>
        <td style="text-align: center;"width=30%;">Clicking on a product from admin home screen</td>
        <td style="text-align: center;"width=30%;">Product edit screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/home.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/product.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/editScreen1.PNG" width="240"></td>
   </tr>
 </table>
 

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicked on get locks</td>
        <td style="text-align: center;"width=30%;">Clicked on get product</td>
        <td style="text-align: center;"width=30%;">Changed product description and name</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/getLocks.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/getProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/changedItems.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Change confirmation popup on click of update product</td>
        <td style="text-align: center;"width=30%;">Clicking yes saved the description</td>
        <td style="text-align: center;"width=30%;">Clicked on release locks</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/confirmationPopup.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/updatedProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/releaseLocks.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Admin home screen with updated product name</td>
        <td style="text-align: center;"width=30%;">Clicking on the updated product from admin home screen</td>
        <td style="text-align: center;"width=30%;">searched for updated product in user flow</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/changedProductInAdmin.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/changedProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/changedProductInUser.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Product edit screen with stocks set to 0</td>
        <td style="text-align: center;"width=30%;">Searched on admin home screen with updated product with 0 stock as not available</td>
        <td style="text-align: center;"width=30%;">Searched on user home screen with updated product with 0 stock as not available</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/editScreen2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/notAvailableProductAdmin.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/notAvailableProductUser.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Product edit screen to change image</td>
        <td style="text-align: center;"width=30%;">Window displayed on click of upload image button and then selected it</td>
        <td style="text-align: center;"width=30%;">New image uploaded and url generated is displayed and image is displayed below 1</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/editScreen3.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/imageToBeUploaded.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/editScreen4.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">New image uploaded and url generated is displayed and image is displayed below 2</td>
        <td style="text-align: center;"width=30%;">Searched on admin home screen with updated product with updated image</td>
        <td style="text-align: center;"width=30%;">Searched on user home screen with updated product with with updated image</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/editFlow/editScreen5.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/changedImageAdmin.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/editFlow/changedImageUser.PNG" width="240"></td>
   </tr>
 </table>

### Delete product flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Searched for the product to be deleted</td>
        <td style="text-align: center;"width=30%;">Clicked on the product from the previous screen and then clicked edit product button</td>
        <td style="text-align: center;"width=30%;">Clicked get locks</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/deleteProductFlow/searchProduct.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/productMagnified.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/editClicked.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicked get product</td>
        <td style="text-align: center;"width=30%;">Clicked on delete product</td>
        <td style="text-align: center;"width=30%;">Delete confirmation popup is displayed on click of delete product form previous screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/deleteProductFlow/getLocksClicked.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/getProductClicked.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/deleteProductClicked.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicked no, nothing happens</td>
        <td style="text-align: center;"width=30%;">Clicked yes, product is deleted and journey is resumed to admin home screen</td>
        <td style="text-align: center;"width=30%;">Deleted product could not be found in the product list</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/deleteProductFlow/clickedNo.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/clickedYes.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/deletedAdmin1.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Searched for the deleted product but is missing in admin flow</td>
        <td style="text-align: center;"width=30%;">Searched for the deleted product but is missing in user flow</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/deleteProductFlow/deletedAdmin2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/deleteProductFlow/deletedUser1.PNG" width="240"></td>
   </tr>
 </table>

### Orders flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Admin orders Screen with nothing selected</td>
        <td style="text-align: center;"width=30%;">Drop down to sort the orders list</td>
        <td style="text-align: center;"width=30%;">Selected By Customer</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/ordersFlow/orderHome.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/orderTypeDropDown.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/ordersByCustomer.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicking on one of the customer shows all the orders of that user</td>
        <td style="text-align: center;"width=30%;">Selected by time on admin orders screen</td>
        <td style="text-align: center;"width=30%;">Selected status filter</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/ordersFlow/withinOrdersByCustomer.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/orderByTime.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/orderByStatusFilter.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Selected multiple options to sort and clicked filter 1</td>
        <td style="text-align: center;"width=30%;">Selected multiple options to sort and clicked filter 2</td>
        <td style="text-align: center;"width=30%;">Auto load option selected removes the work of clicking on filter but increases gateway calls</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/ordersFlow/statusFilterSelection1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/statusFilterSelection2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/autoLoadFunctionality.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Selected clean on admin orders screen</td>
        <td style="text-align: center;"width=30%;">Selected the top product</td>
        <td style="text-align: center;"width=30%;">Order Summary 1</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/ordersFlow/clean.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/withinOrdersByCustomer.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/orderSummary1.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Order Summary 2</td>
        <td style="text-align: center;"width=30%;">Changed status of individual product by tapping order/deliver status beside the product</td>
        <td style="text-align: center;"width=30%;">Changed status of entire order by tapping order/deliver status on the top and clicking on synchronize</td>
        
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/ordersFlow/orderSummary2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/changedIndividualStatus.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/changedOrderStatus1.PNG" width="240"></td>
        
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Changed status of entire order by tapping order/deliver status on the top</td>
        <td style="text-align: center;"width=30%;">Click on synchronize to reflect on individual product</td>
        <td style="text-align: center;"width=30%;">Change is reflected in orders list</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/ordersFlow/changedOrderStatus2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/updatedOrderList.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/ordersFlow/userOrderList.PNG" width="240"></td>
   </tr>
 </table>
 
### Functions flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Functions screen</td>
        <td style="text-align: center;"width=30%;">Edit contact us details screen</td>
        <td style="text-align: center;"width=30%;">Contact us screen for user</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/functions.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/contactDetails.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/contactUsUser.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Changed contact us details and clicked on update contact details</td>
        <td style="text-align: center;"width=30%;">Change reflected on contact us screen for user</td>
        <td style="text-align: center;"width=30%;">Edit pincode Screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/editContactUs.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/updatedContactUsUser.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/editPinCode.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">User profile screen with valid pincode</td>
        <td style="text-align: center;"width=30%;">Changed to invalid pincode</td>
        <td style="text-align: center;"width=30%;">Inserted that invalid pincode from edit pincode screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/workingPincode.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/pinCodeChanged.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/editPincodeAdd1.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">New added pincode is displayed in the below list</td>
        <td style="text-align: center;"width=30%;">New added pincode is reflected as valid in the user profile</td>
        <td style="text-align: center;"width=30%;">Edit pincode screen</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/editPincodeAdd2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/workingPincode2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/revertPincode.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Clicked on minus sign in front of the pincode added to delete it and confirmation popup is shown</td>
        <td style="text-align: center;"width=30%;">Clicking on yes deleted the pincode</td>
        <td style="text-align: center;"width=30%;">Deleted pincode is now regarded as invalid</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/revertPincodePopup.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/removedPincode.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/removePinCodeError.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Changing to valid pincode makes the profile complete again</td>
        <td style="text-align: center;"width=30%;">Spam settings screen</td>
        <td style="text-align: center;"width=30%;">Changed lock limit from 20 to 2 tells how many times user can place order of same product in single day</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/workingPinCodeUpdated.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/spamSettings1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/spamSettingsEdited.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Cart 1</td>
        <td style="text-align: center;"width=30%;">Cart 2</td>
        <td style="text-align: center;"width=30%;">Cart 3</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/cart1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/cart2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/cart3.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Confirm order with product which has been ordered large number of times shown as spam detected</td>
        <td style="text-align: center;"width=30%;">Confirm order 1</td>
        <td style="text-align: center;"width=30%;">Confirm order 2</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/confirmOrder1.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/confirmOrder2.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/confirmOrder3.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Changed spam settings back to 20</td>
        <td style="text-align: center;"width=30%;">Checkout 1</td>
        <td style="text-align: center;"width=30%;">Checkout 2</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/spamSettingsUpdated.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/cart4.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/cart5.PNG" width="240"></td>
   </tr>
 </table>

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Limit is increased so not products are not shown under spam 1</td>
        <td style="text-align: center;"width=30%;">Limit is increased so not products are not shown under spam 2</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/functionsFlow/confirmOrder4.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/functionsFlow/confirmOrder5.PNG" width="240"></td>
   </tr>
 </table>
   
### Developer flow  

<table>
   <tr>
        <td style="text-align: center;"width=30%;">Developer screen access confirmation popup</td>
        <td style="text-align: center;"width=30%;">Clicking no on popup from previous screen</td>
        <td style="text-align: center;"width=30%;">Developer Screen by clicking yes on popup</td>
   </tr>
   <tr>
        <td><img style="text-align: center;" src="images/developerFlow/developerFlow.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/developerFlow/developerPopupClickedNo.PNG" width="240"></td>
        <td><img style="text-align: center;" src="images/developerFlow/developerPopupClickedYes.PNG" width="240"></td>
   </tr>
 </table>
 