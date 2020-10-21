scoreboard objectives add ranIndevSetup dummy
execute unless score runIndevSetup ranIndevSetup matches 1 run scoreboard players set runIndevSetup ranIndevSetup 0
execute if score runIndevSetup ranIndevSetup matches 0 run function modern_beta:setup_house
scoreboard players set runIndevSetup ranIndevSetup 1
function modern_beta:setup