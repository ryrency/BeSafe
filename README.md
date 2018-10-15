# BeSafe

BeSafe is a <b>Safety App</b> which at the first time of launching the app takes in your information and your emegrgency contact information.
At this time, it also presents a tutorial on how to use the app.
There after, it displays the emergency button page everytime you launch the app.<br><br>
If you ever feel threatened / not safe, all you need to do is click on the Emergency button and an SOS message along with your location is 
sent to your emergency contact. Incase you are safe now / you clicked the button in error, you can cancel the alert which will send another message to your contact informing them that you are now safe.
To cancel, you would need to enter the passcode you set while the app setup to ensure that it is you who is cancelling the alert. <br>

Once the emergency is clicked, your location is tracked regularly using the LocationMonitoringService until you indicate that you are safe and this data is sent to cloud.<br>
Volley is used for all network connection activities.<br>
Recycler view is used for displaying the tutorial.<br>
<br>Another feature included is the record activity feature in which you can record any previous unusual activity that is no longer an emergency and this information
is logged onto cloud that could be used for making the area safer by the police / authorities.
