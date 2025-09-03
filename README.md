# City Search

City Search is an Android application built with Kotlin and Jetpack Compose that allows users to quickly search for cities and view their locations on Google Maps.  
The project is designed to showcase problem-solving skills, UX judgment, and clean code practices.

## Features

- Load a list of cities from a local JSON file.
- Search and filter results by a given prefix string.
- Display cities in a scrollable list, sorted alphabetically.
- Show **city name + country code** as the title.
- Show **latitude and longitude** as the subtitle.
- Open the selected city’s location directly in Google Maps when tapped.

## Data Structure and Search Algorithm

### Trie (Prefix Tree)

City Search uses a **Trie (prefix tree)** data structure to optimize searching by city name.  
This enables fast, efficient, and scalable prefix-based lookups.

**Why Trie?**
- **Efficient Lookups** – Search complexity is **O(m)**, where `m` is the length of the search prefix.
- **Memory Friendly** – Common prefixes are stored only once, reducing redundancy.
- **Scalability** – Handles large datasets of cities without sacrificing performance.

## Getting Started

### Open with Android Studio
1. Open **Android Studio**  
2. Click **Get from VCS** (or *File → New → Project from Version Control*)  
3. Paste the repo link: https://github.com/noureldensaid/City-Search.git
4. Choose afolder and click **Clone**  
6. Let Gradle sync, then **Run** the app

## ScreenShots
<img width="1209" height="2553" alt="1" src="https://github.com/user-attachments/assets/e78a32f2-3d62-4b92-a0e1-d01284423a0d" />
<img width="1209" height="2553" alt="2" src="https://github.com/user-attachments/assets/76b4b18f-6072-4298-a88f-e7dc2b4ebe78" />
<img width="1209" height="2553" alt="4" src="https://github.com/user-attachments/assets/de8c7415-e72b-436c-b081-a9fa2c0b577b" />
<img width="2553" height="1209" alt="3" src="https://github.com/user-attachments/assets/a582ecba-6349-4ace-977c-ee80a0bb11d2" />

