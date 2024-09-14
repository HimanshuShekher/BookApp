# Book App using Jetpack Compose

**Book** is an Android application that allows users to view, favorite, and manage books. The app integrates various features to provide a seamless and user-centric experience.

## Features

- **User Authentication**: Secure login and registration with encrypted credentials.
- **User-Specific Favorites**: Users can mark books as favorites, with favorites stored and retrieved based on the logged-in user.
- **Dynamic Book Lists**: Fetches and displays books from an API, with sorting and grouping by year.
- **Interactive UI**: Utilizes **Jetpack Compose** for a modern, responsive user interface.
- **Default Country Detection**: Automatically sets the default country based on the user's IP location for a personalized experience.

![WhatsApp Image 2024-09-14 at 23 50 22 (3)](https://github.com/user-attachments/assets/fe78efa6-e01c-43ee-8142-e7a5b051c8c2)

![WhatsApp Image 2024-09-14 at 23 50 22 (2)](https://github.com/user-attachments/assets/59bbfba7-7fae-40d8-b013-0f81a8765acb)


## Architecture

- **MVVM**: Follows the **Model-View-ViewModel** architecture to separate concerns and manage UI-related data efficiently.
- **Hilt**: Manages dependency injection with **Hilt** to simplify code and improve modularity.
- **Encrypted SharedPreferences**: Ensures secure storage of user credentials and ID.
- **IP Location and Country Integration**: Uses IP-based geolocation to automatically determine the default country, enhancing user experience by pre-selecting relevant country data.

## Libraries and Tools

- **Jetpack Compose**: For building a declarative and responsive UI.
- **Hilt**: For dependency injection.
- **Retrofit**: For handling API requests and responses.
- **Kotlin Coroutines**: For managing asynchronous operations.
- **EncryptedSharedPreferences**: For secure storage of sensitive user data.
- **IP Location API**: To determine the default country based on the user's IP address.

![WhatsApp Image 2024-09-14 at 23 50 22 (1)](https://github.com/user-attachments/assets/244cdd8f-b4b1-4dd7-8655-e16612e0605c)

![WhatsApp Image 2024-09-14 at 23 50 21](https://github.com/user-attachments/assets/7add974c-2f75-4f6d-aa2c-8483c96f14b6)

