gamerule spawnRadius 0
setworldspawn ~ ~ ~ -180

setblock ~ ~-1 ~ minecraft:structure_block[mode="load"]{name:"modern_beta:indev_house",ignoreEntities:0b,mode:"LOAD",rotation:NONE, posX:-3, posZ:-3}
execute if block ~ ~-1 ~ structure_block run setblock ~-1 ~-1 ~ minecraft:redstone_block