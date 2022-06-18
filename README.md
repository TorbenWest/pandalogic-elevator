# Elevator [![Java CI with Maven](https://github.com/TorbenWest/pandalogic-elevator/actions/workflows/maven.yml/badge.svg)](https://github.com/TorbenWest/pandalogic-elevator/actions/workflows/maven.yml)

This plugin gives you the opportunity to dynamically create elevators in your worlds.

## Commands

To use this plugin, you have the following commands available:

### /elevator help

Displays all the listed commands in your chat.

### /elevator create \<CurrentY> \<BLX> \<BLY> \<BLZ> \<TRX> \<TRY> \<TRZ>

Creates an elevator. The **current y** is the height where the lowest block of the elevator car is located. This height
will be compared with the floor heights, when the elevator is moving. <br>
The abbreviation **BLX** stands for **BottomLeftX**. This should be the bottom left corner of the elevator car.
Accordingly, **TRX** stands for **TopRightX** and is the top right corner. You can also think of the position one and
two. Thus, the complete elevator car can be measured and the plugin knows where the elevator car is located. After the
creation, you will receive the elevator id which you need for the following commands.

### /elevator delete \<Id>

This command deletes a registered elevator. To do so, you have to provide the correct **elevator id**.

### /elevator createFloor \<ElevatorId> \<FloorNumber> \<BaseY> \<BX> \<BY> \<BZ> \<GateBLX> \<GateBLY> \<GateBLZ> \<GateTRX> \<GateTRY> \<GateTRZ> \<OutputDirection>

This command creates a floor for an existing elevator. The first argument will be the **elevator id**, which you receive
either after creating the elevator or by the _/elevator identify_ command. The **floor number** is your floor number,
here only integers are allowed. That means, you cannot use for example _EG_, _U1_, etc. Use _0_ or _-1_ instead. <br>
The **base y** is the height you are standing on. In other words, the height where the elevator car will stop tho you
can go inside or outside without jumping. <br>
**BX** means **ButtonX**, each floor will have a button to call the elevator. Here, you have to specify the coordinates
of this button. The button can be of any type (_OAK_BUTTON_, _STONE_BUTTON_, etc.). <br>
**GateBLX** is **GateBottomLeftX** or **GateTopRightX** for the following the arguments. This specifies the location of
the floor entrance, called gate. Keep in mind, that the elevator itself has no gate, just the floor. <br>
The last argument is the **output direction**. This can have the value _NORTH_, _EAST_, _SOUTH_, or _WEST_. The output
direction is the direction when you are in the elevator, and you look to the floor exit. This is important, because as
soon as the elevator closes the floor, the player which are in the floor will be pushed in- or outside the elevator.

### /elevator deleteFloor \<ElevatorId> \<FloorNumber>

This command deletes a registered floor from an elevator. Here, you also have to provide the **elevator id** and the
correct **floor number**.

### /elevator identify

If you want to get the id from an existing elevator, you can go inside the elevator car and run the command. If the
elevator isn't registered yet, you will be notified.

### /elevator info \<Id>

This command provides all known details about an elevator. Again, you have to provide **elevator id**. You will get
details about the registered floors and settings.

### /elevator inventory

This command opens the inventory to control the elevator. You will get an inventory with minecarts. Each minecraft
represent one floor. When a minecart has a glow effect, it means that the elevator has already called from this floor or
the elevator is currently at this floor located. By clicking on a minecart, you call the elevator to the certain floor.

### /elevator settings \<ElevatorId> \<Setting> \<Value>

Each elevator has five settings. By using this command, you can modify each of them. The **elevator id** will specify
your elevator. The **setting** and **value** argument specifies which setting should get what value. <br>

| Setting               | Description | Default value |
| ---                   |---|---|
| speed                 | The speed of the elevator given in Minecraft ticks. For example, if the value is set to 10, the speed of the elevator will be 2 blocks per second. | 15 Ticks |
| start_delay           | The delay after the gates closed, until the elevator will start to move. | 40 Ticks |
| max_player            | The maximum amount of player, allowed in this elevator. If more players are in the elevator, the elevator will wait until a player is leaving it. | 5 Players |
| next_stop_check       | When the elevator stops at a floor, this will be the delay until the elevator will automatically check for a new call, if it's not already moving again. | 60 Ticks |
| player_position_check | When the elevator is moving it can happen, for example, due to server lags or a high ping from the player, that a player will glitch out of the elevator car. Therefore, the elevator automatically checks in an interval of this ticks, if this happened and if so the player will be teleported back into the elevator car. | 5 Ticks |
