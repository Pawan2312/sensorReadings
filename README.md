# sensorReadings
This is an android app which can connect to nodemcu or any other wifi enabled chip and can transfer data from the microcontroller into this app. Currently I'm using to monitor my underground tank and over head tank with this app, though it can be used for any other sensor similarly.

As of now this app uses HTTP (OKHTTP) as it's backend to fetch data from the nodecmu chip via HTTP get call. But I'm trying to implement it with Websockets for more convenience which can run in the background and keep connected to the nodemcu and get updates in real time.

This app parses JSON fetched from the nodemcu via HTTP call, and shows it in the recycler view.

I have used recycler veiw to dynamically add new readings and show it in the view.

You are welcome to edit my code. And help in making this app better.



ScreenShot of this app!



<img width="461" alt="image" src="https://user-images.githubusercontent.com/38160877/118514003-ce852b80-b751-11eb-90af-fb66bac085a8.png">
