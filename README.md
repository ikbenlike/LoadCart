# LoadCart
Simple spigot plugin for loading chunks for minecart travel.

This plugin has not been developed or tested with mods in mind. Mods that add or change minecart (behaviour) may cause this
plugin to behave in unexpected ways.

## Commands

This plugin provides several custom commands.

- countminecarts
  - Description: Count the amount of minecarts in loaded chunks
  - Alias: cmc
- countemptycarts
  - Description: Count the amount of empty minecarts in loaded chunks
  - Alias: cemc
- countutilitycarts
  - Description: Count the amount of utility carts in loaded chunks
  - Alias: cumc
- removeminecarts
  - Description: Remove all minecarts in loaded chunks
  - Alias: rmc
- removeemptycarts
  - Description: Remove all empty minecarts in loaded chunks
  - Alias: remc
- removeutilitycarts
  - Description: Remove all utility minecarts in loaded chunks
  - Alias: rumc
- removetickets
  - Description: Remove all tickets added by this plugin
  - Alias: rts 


## Permissions

This plugin provides the following three permissions:

### LoadCart.count

This is the permission necessary for all the cart counting commands (commands starting with "count" or alias starting with "c").
By default, it is granted to everyone.

### LoadCart.remove

This is the permission necessary for all the cart removal commands (commands starting with "remove" or alias starting with "r").
By default, it is only granted to server operators.

### LoadCart.ticket

This is the permission necessary for all the ticket management commands. By default, it is only granted to server operators.

## Configuration

This plugin offers two configuration flags.

### 1. `chunks`

This option needs to be set to true for minecarts to load chunks. It is set to true by default.

### 2. `verbose`

This option needs to be set to true to receive detailed feedback in the server console. It is set to false by default.