
# Overview
**Main objective**:  Using the language of your choice, either Java or Kotlin, develop an android application that queries the device contacts provider, saves them into a list and uploads the list to a fictitious URL of your choice. ✔️ 

**Optional add-on #1**: Upload the content of all notifications received to the same fictitious URL of your choice. ✔️

**Optional add-on #2**: Using an encryption method of your choice, encrypt all the strings used in the application. The methods for encryption and decryption should be available in the code. ❌ 
***(Encryption/Decryption functions was implemented but weren't used)***

**Optional add-on #3**: Implement your own custom method using reflection which encodes the application package name with base64. Upload the encoded string to the same fictitious URL of your choice. ✔️



# Funcionalities Overview
The image below shows the graphical functionalities of the application, followed by a description of each one:

<img src="https://github.com/tiagompconceicao/Technical-Test/blob/main/FuncionalityOverview.jpg?raw=true" width="200" height="400">

- 1: EditText that contains the URL
- 2: Button to send all notifications catched to the URL
- 3: Button to generate pseudo randomly a new URL
- 4: Button to send all contacts loaded to the URL
- 5: Button to send the package name encoded in base64 to the URL
- 6: List of all notifications catched
- 7: List of all contacts loaded

# Architectural Overview

This application was developed using Kotlin and follows a MVVM design pattern (Model-View-ViewModel), and has the next architecture:

- Activity
- ViewModel
- NotificationListener
- View
- Model
- Repository
- Cryptography

### Activity
Was implemented only 1 activity in this application, which graphical features was presented and described in the [Functionalities Overview](#funcionalities-overview) section.

### ViewModel
Class to store the contacts and notification (using LiveData fields), also perform the business logic, as call the Repository methods to send these fields remotely, or instanciate a new URL. 

### NotificationListener
Class to instanciate a service to listen notifications.
When a notification is catched, it is sent to the MainActivity by a broadcast receiver, then is stored in the respective field of the viewModel instance.

### View
Contains the classes used to instantiate the view objects of the contacts and notifications lists.

### Model
Contains the necessary data classes.

### Repository
Class that performs external contact aside the application, as load the contacts from the device, or send some info to a URL.

### Cryptography
Contains the functions to encrypt and decrypt using the AES algorithm.
