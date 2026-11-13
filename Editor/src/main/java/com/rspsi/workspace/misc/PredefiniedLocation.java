package com.rspsi.workspace.misc;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.table.DefaultTableModel;
import java.util.*;

public class PredefiniedLocation {
        private final DefaultTableModel model = new DefaultTableModel(
                new Object[][]{
                        {
                                "3222,3217,0",
                                "Lumbridge"
                        }, {
                        "3212,9620,0",
                        "Lumbridge Kitchen Dungeon"
                }, {
                        "3210,3424,0",
                        "Varrock"
                }, {
                        "3232,9801,0",
                        "Blue Moon Inn Dungeon"
                }, {
                        "3190,9834,0",
                        "Varrock West Bank Dungeon"
                }, {
                        "3237,9859,0",
                        "Varrock Sewers Dungeon"
                }, {
                        "3244,9782,0",
                        "Black Arm Gang Hideout"
                }, {
                        "3293,3164,0",
                        "Al-Kharid"
                }, {
                        "3093,3244,0",
                        "Draynor Village"
                }, {
                        "3109,3361,0",
                        "Draynor Manor"
                }, {
                        "3104,9754,0",
                        "Under Draynor Manor"
                }, {
                        "3078,9772,0",
                        "Count Draynor Dungeon"
                }, {
                        "2957,3214,0",
                        "Rimmington"
                }, {
                        "3080,3424,0",
                        "Barbarian Village"
                }, {
                        "3121,3210,0",
                        "Wizards' Tower"
                }, {
                        "3104,9576,0",
                        "Wizards' Tower Dungeon"
                }, {
                        "2804,3434,0",
                        "Catherby"
                }, {
                        "3143,3443,0",
                        "Cooks' Guild"
                }, {
                        "2964,3378,0",
                        "Falador"
                }, {
                        "3009,3150,0",
                        "Asgarnian Ice Dungeon Entrance"
                }, {
                        "3009,9550,0",
                        "Asgarnian Ice Dungeon"
                }, {
                        "3021,9739,0",
                        "Mining Guild Dungeon"
                }, {
                        "2956,3506,0",
                        "Goblin Village"
                }, {
                        "3027,3217,0",
                        "Port Sarim"
                }, {
                        "3001,3144,0",
                        "Thurgo's Peninsula"
                }, {
                        "2956,3146,0",
                        "Musa Point"
                }, {
                        "2913,3170,0",
                        "Karamja"
                }, {
                        "3087,3507,0",
                        "Edgeville"
                }, {
                        "3097,9869,0",
                        "Edgeville Dungeon"
                }, {
                        "3052,3491,0",
                        "Edgeville Monastary"
                }, {
                        "3191,3363,0",
                        "Champions' Guild"
                }, {
                        "2851,3238,0",
                        "Crandor"
                }, {
                        "2833,3257,0",
                        "Crandor Dungeon Entrance"
                }, {
                        "2833,9656,0",
                        "Crandor Dungeon"
                }, {
                        "2936,3249,0",
                        "Melzar's Maze"
                }, {
                        "2929,9649,0",
                        "Melzar's Maze Dungeon"
                }, {
                        "3047,9640,1",
                        "Lady Lumbridge"
                }, {
                        "3047,9640,2",
                        "Lady Lumbridge"
                }, {
                        "3047,9640,3",
                        "Lady Lumbridge"
                }, {
                        "2867,9955,0",
                        "Ice Queen Lair"
                }, {
                        "2891,3455,0",
                        "Taverley"
                }, {
                        "2884,9798,0",
                        "Taverly Dungeon"
                }, {
                        "2834,3335,0",
                        "Entrana"
                }, {
                        "2832,9770,0",
                        "Entrana Dungeon"
                }, {
                        "2451,4468,0",
                        "Zanaris Dungeon"
                }, {
                        "2757,3477,0",
                        "Camelot"
                }, {
                        "2793,9819,0",
                        "Merlin's Crystal Crates"
                }, {
                        "2778,9839,0",
                        "Merlin's Crystal Crates"
                }, {
                        "2897,3511,0",
                        "Heroes' Guild"
                }, {
                        "2917,9912,0",
                        "Heroes' Guild Dungeon"
                }, {
                        "2933,3289,0",
                        "Crafting Guild"
                }, {
                        "2772,3234,0",
                        "Brimhaven"
                }, {
                        "2706,9564,0",
                        "Brimhaven Dungeon"
                }, {
                        "2906,9876,0",
                        "Witch's House Dungeon"
                }, {
                        "2725,3491,0",
                        "Seer's Village"
                }, {
                        "2702,3400,0",
                        "Sorcerer's Tower"
                }, {
                        "3088,3570,0",
                        "Obelisk Of Air"
                }, {
                        "2842,3423,0",
                        "Obelisk Of Water"
                }, {
                        "3085,9932,0",
                        "Obelisk Of Earth"
                }, {
                        "2821,9827,0",
                        "Obelisk Of Fire"
                }, {
                        "2545,3569,0",
                        "Barbarian Outpost"
                }, {
                        "2548,9951,0",
                        "Barbarian Agility Dungeon"
                }, {
                        "2662,3305,0",
                        "Ardougne"
                }, {
                        "2696,9683,0",
                        "Family Crest Dungeon"
                }, {
                        "2876,3482,0",
                        "White Wolf Mountain Shortcut"
                }, {
                        "2876,9879,0",
                        "White Wolf Mountain Dungeon Shortcut"
                }, {
                        "2820,3486,0",
                        "White Wolf Mountain Shortcut"
                }, {
                        "2821,9882,0",
                        "White Wolf Mountain Dungeon Shortcut"
                }, {
                        "2677,3404,0",
                        "Temple Of Ikov Dungeon Entrance"
                }, {
                        "2677,9804,0",
                        "Temple Of Ikov Dunegon"
                }, {
                        "2641,9764,0",
                        "Temple Of Ikov Dungeon Lit"
                }, {
                        "2641,9740,0",
                        "Temple Of Ikov Dungeon Dark"
                }, {
                        "2611,3393,0",
                        "Fishing Guild"
                }, {
                        "2633,4685,0",
                        "Holy Grail Distand Land Alive"
                }, {
                        "2803,4723,0",
                        "Holy Grail Distant Land Dying/Dead"
                }, {
                        "2585,3150,0",
                        "Fight Arena"
                }, {
                        "2542,3170,0",
                        "Tree Gnome Village"
                }, {
                        "2596,4435,0",
                        "Gnome Dungeon"
                }, {
                        "2529,3307,0",
                        "West Ardougne"
                }, {
                        "2514,9740,0",
                        "Ardougne Western Sewers Dungeon New"
                }, {
                        "2537,9670,0",
                        "West Ardougne Dungeon"
                }, {
                        "2569,3273,0",
                        "Home Of The Carnilleans"
                }, {
                        "2569,9668,0",
                        "Home Of The Carnilleans Dungeon"
                }, {
                        "3094,3107,0",
                        "Tutorial Island"
                }, {
                        "3088,9520,0",
                        "Tutorial Island Dungeon"
                }, {
                        "3014,3849,0",
                        "King Black Dragon Entrance"
                }, {
                        "3069,10255,0",
                        "King Black Dragon Lever"
                }, {
                        "2782,3273,0",
                        "Fishing Platform"
                }, {
                        "2795,3321,0",
                        "Rowboat"
                }, {
                        "2575,9861,0",
                        "Waterfall Dungeon"
                }, {
                        "2555,9844,0",
                        "Glarial's Tomb Dungeon"
                }, {
                        "2539,9908,0",
                        "Chalice Of Eternity Flooded"
                }, {
                        "2604,9907,0",
                        "Chalice Of Eternity High Ground"
                }, {
                        "2606,3093,0",
                        "Yanille"
                }, {
                        "2590,3088,0",
                        "Wizards' Guild"
                }, {
                        "2593,9485,0",
                        "Wizards' Guild Dungeon"
                }, {
                        "2516,3369,0",
                        "King Lathas's Training Grounds"
                }, {
                        "2830,9522,0",
                        "Jogre Dungeon"
                }, {
                        "2789,3066,0",
                        "Tai Bwo Wannai"
                }, {
                        "2602,9479,0",
                        "Yanille Agility Dungeon"
                }, {
                        "2578,9582,0",
                        "Yanille Agility Dungeon"
                }, {
                        "2616,9568,0",
                        "Yanille Agility Dungeon"
                }, {
                        "2461,3443,0",
                        "Tree Gnome Stronghold"
                }, {
                        "2474,3436,0",
                        "Gnome Stronghold Agility Course"
                }, {
                        "2409,9814,0",
                        "Brimstail's Cave Dungeon"
                }, {
                        "2465,3495,0",
                        "Grand Tree"
                }, {
                        "2470,9899,0",
                        "Grand Tree Dungeon"
                }, {
                        "2898,9401,0",
                        "Ah Za Rhoon"
                }, {
                        "2889,9285,0",
                        "Ah Za Rhoon"
                }, {
                        "2852,2954,0",
                        "Shilo Village"
                }, {
                        "2763,9375,0",
                        "Tomb Of Bervirius Dungeon"
                }, {
                        "2928,9521,0",
                        "Rashiliyia's Tomb Dungeon"
                }, {
                        "2998,3916,0",
                        "Wilderness Agility Course"
                }, {
                        "3005,10362,0",
                        "Wilderness Agility Dungeon"
                }, {
                        "2447,3175,0",
                        "Observatory"
                }, {
                        "2419,9458,0",
                        "Observatory Dungeon"
                }, {
                        "2435,3315,0",
                        "Underground Pass Entrance"
                }, {
                        "2495,9715,0",
                        "Underground Pass Entrance"
                }, {
                        "2415,9674,0",
                        "Underground Pass"
                }, {
                        "2370,9719,0",
                        "Underground Pass"
                }, {
                        "2483,9607,0",
                        "Underground Pass"
                }, {
                        "2476,9635,0",
                        "Underground Pass"
                }, {
                        "2321,9805,0",
                        "Underground Pass"
                }, {
                        "2139,4647,1",
                        "Iban's Throne"
                }, {
                        "2338,9623,0",
                        "Underground Pass"
                }, {
                        "2168,4726,0",
                        "Underground Pass"
                }, {
                        "3304,3123,0",
                        "Shantay Pass"
                }, {
                        "3175,3041,0",
                        "Bedabin Camp"
                }, {
                        "3286,3023,0",
                        "Desert Mining Camp"
                }, {
                        "3278,9427,0",
                        "Desert Mining Camp Cave"
                }, {
                        "2549,3113,0",
                        "Watchtower"
                }, {
                        "2933,4714,2",
                        "Revived Watchtower"
                }, {
                        "2519,9436,0",
                        "Skavid Caves"
                }, {
                        "2587,9449,0",
                        "Ogre Enclave"
                }, {
                        "2516,3044,0",
                        "Gu'tanoth"
                }, {
                        "2572,3463,0",
                        "Dwarf Cannon"
                }, {
                        "2620,9797,0",
                        "Goblin Cave Dungeon"
                }, {
                        "2741,3554,0",
                        "Sinclair Mansion"
                }, {
                        "3354,3402,0",
                        "Digsite"
                }, {
                        "3370,9828,0",
                        "The Dig Site Dungeon 1"
                }, {
                        "3370,9764,0",
                        "The Dig Site Dungeon 2"
                }, {
                        "3477,9845,0",
                        "The Dig Site Dungeon 3"
                }, {
                        "3348,3345,0",
                        "Exam Centre"
                }, {
                        "2665,3161,0",
                        "Port Khazard"
                }, {
                        "2666,3161,0",
                        "Fishing Trawler Minigame"
                }, {
                        "1888,4825,0",
                        "Trawler Boat Empty"
                }, {
                        "2016,4825,0",
                        "Trawler Boat Leak"
                }, {
                        "1953,4825,0",
                        "Trawler Boat Sunk"
                }, {
                        "3311,3504,0",
                        "Sawmill"
                }, {
                        "2729,3376,0",
                        "Legend's Guild"
                }, {
                        "2727,9774,0",
                        "Legend's Guild Dungeon"
                }, {
                        "2772,9341,0",
                        "Viyeldi Caves 1"
                }, {
                        "2377,4712,0",
                        "Viyeldi Caves 2"
                }, {
                        "3107,3937,0",
                        "Mage Arena"
                }, {
                        "2536,4716,0",
                        "Mage Arena Bank"
                }, {
                        "2509,4692,0",
                        "Mage Arena Chamber"
                }, {
                        "3345,3251,0",
                        "Duel Arena"
                }, {
                        "3176,2987,0",
                        "Bandit Camp"
                }, {
                        "2911,4832,0",
                        "Rune Essence Mine"
                }, {
                        "2841,4829,0",
                        "Air Altar"
                }, {
                        "2793,4828,0",
                        "Mind Altar"
                }, {
                        "2726,4832,0",
                        "Water Altar"
                }, {
                        "2655,4830,0",
                        "Earth Altar"
                }, {
                        "2583,4840,0",
                        "Fire Altar"
                }, {
                        "2523,4826,0",
                        "Body Altar"
                }, {
                        "2162,4833,0",
                        "Cosmic Altar"
                }, {
                        "2269,4843,0",
                        "Chaos Altar"
                }, {
                        "2400,4835,0",
                        "Nature Altar"
                }, {
                        "2464,4818,0",
                        "Law Altar"
                }, {
                        "2630,2997,0",
                        "Feldip Hills Cave Dungeon Entrance"
                }, {
                        "2647,9378,0",
                        "Feldip Hills Cave Dungeon"
                }, {
                        "3494,3476,0",
                        "Canifis"
                }, {
                        "3408,3489,0",
                        "Paterdomus"
                }, {
                        "3405,9906,0",
                        "Paterdomus Dungeon"
                }, {
                        "2720,9889,0",
                        "Elemental Worshop I Dungeon"
                }, {
                        "2762,9558,0",
                        "Brimhaven Agility Course Dungeon"
                }, {
                        "3442,9737,0",
                        "Nature Spirit Altar Dead"
                }, {
                        "3442,9737,1",
                        "Nature Spirit Altar Alive"
                }, {
                        "2898,3546,0",
                        "Burthorpe"
                }, {
                        "2890,3676,0",
                        "Trollheim"
                }, {
                        "2269,4752,0",
                        "Saba's Cave Dungeon"
                }, {
                        "2904,3643,0",
                        "Trollheim Cave Entrance"
                }, {
                        "2907,10019,0",
                        "Trollheim Cave Dungeon Entrance"
                }, {
                        "2833,10092,0",
                        "Troll Stronghold Caves/Dungeon"
                }, {
                        "2837,10090,2",
                        "Troll Stronghold Entrance Floor 1"
                }, {
                        "2841,10108,1",
                        "Troll Stronghold Floor 2"
                }, {
                        "2852,10105,0",
                        "Troll Stronghold Floor 3"
                }, {
                        "3044,4973,1",
                        "Rouges Den"
                }, {
                        "2912,4576,0",
                        "Maze Random Event"
                }, {
                        "2008,4762,0",
                        "Mime Artist's Stage"
                }, {
                        "2242,3278,0",
                        "Prifddinas"
                }, {
                        "2197,3252,0",
                        "Elf Camp"
                }, {
                        "2186,3147,0",
                        "Tyras Camp"
                }, {
                        "2309,4597,0",
                        "Regicide Cut Scene"
                }, {
                        "3226,3107,0",
                        "Kalphite Lair Entrance"
                }, {
                        "3484,9509,2",
                        "Kalphite Lair Entrance Dungeon"
                }, {
                        "2766,2980,0",
                        "Cairn Isle"
                }, {
                        "2764,9376,0",
                        "Cairn Isle Dungeon"
                }, {
                        "2527,4547,0",
                        "Tai Bwo Wannai Trio Cutscene"
                }, {
                        "3488,3289,0",
                        "Mort'ton"
                }, {
                        "3496,9674,0",
                        "Shades Of Mort'ton Dungeon"
                }, {
                        "2893,10074,2",
                        "Eadgar's Cave Dungeon"
                }, {
                        "2893,10074,0",
                        "Eadgar's Cave Dungeon"
                }, {
                        "2863,10090,0",
                        "Troll Stronghold Storeroom"
                }, {
                        "2208,4950,0",
                        "Burthorpe Games Room"
                }, {
                        "2652,10003,0",
                        "Swensen's Maze Of Portals"
                }, {
                        "2671,10099,0",
                        "Fremennik Trials Arena Dungeon"
                }, {
                        "2659,3661,0",
                        "Rellekka"
                }, {
                        "2512,3860,0",
                        "Miscellania"
                }, {
                        "2603,3876,0",
                        "Etceteria"
                }, {
                        "2509,3635,0",
                        "Lighthouse"
                }, {
                        "2519,4619,1",
                        "Dagannoth Lighthouse Dungeon"
                }, {
                        "2516,4635,0",
                        "Dagannoth Lighthouse Dungeon"
                }, {
                        "2519,9995,1",
                        "Dagannoth Lighthouse Dungeon"
                }, {
                        "2515,10007,0",
                        "Dagannoth Lighthouse Dungeon"
                }, {
                        "2445,4594,0",
                        "Lighthouse Area"
                }, {
                        "2798,2798,1",
                        "Ape Atoll"
                }, {
                        "2764,9103,0",
                        "Ape Atoll Dungeon"
                }, {
                        "2807,9201,0",
                        "Temple Of Marimbo Dungeon"
                }, {
                        "2893,2724,0",
                        "Crash Island"
                }, {
                        "2647,4511,0",
                        "Gnome Glider Hangar Open"
                }, {
                        "2393,9883,0",
                        "Gnome Glider Hangar Closed"
                }, {
                        "2706,9180,1",
                        "Monkey Madness Demon"
                }, {
                        "2440,3090,0",
                        "Castle Wars Lobby"
                }, {
                        "2400,3103,0",
                        "Castle Wars Field"
                }, {
                        "2400,9504,0",
                        "Castle Wars Dungeon"
                }, {
                        "2379,9489,0",
                        "Castle Wars - Saradomin Waiting Room"
                }, {
                        "2421,9524,0",
                        "Castle Wars - Zamorak Waiting Room"
                }, {
                        "3436,9637,0",
                        "Abandoned Mine Level 1"
                }, {
                        "2774,4577,0",
                        "Abandoned Mine Level 2"
                }, {
                        "2734,4502,0",
                        "Abandoned Mine Level 3"
                }, {
                        "2797,4529,0",
                        "Abandoned Mine Level 4"
                }, {
                        "2723,4452,0",
                        "Abandoned Mine Level 5"
                }, {
                        "2712,4593,0",
                        "Abandoned Mine Dungeon Dark Right"
                }, {
                        "2730,4562,0",
                        "Abandoned Mine Dungeon Dark Left"
                }, {
                        "2792,4427,0",
                        "Abandoned Mine Level 6"
                }, {
                        "2803,10189,0",
                        "Troll Romance Cave"
                }, {
                        "3550,3548,0",
                        "Fenkenstrain's Castle"
                }, {
                        "3477,9845,0",
                        "The Myreque Shortcut"
                }, {
                        "3577,9927,0",
                        "Experiment Cave"
                }, {
                        "3549,9865,0",
                        "Werewolf Agility Course"
                }, {
                        "2808,10002,0",
                        "Fremennik Slayer Dungeon"
                }, {
                        "3684,3502,0",
                        "Port Phasmatys"
                }, {
                        "3662,3519,0",
                        "Ectofuntus"
                }, {
                        "3669,9888,3",
                        "Ectofuntus Dungeon Level 3"
                }, {
                        "3671,9888,2",
                        "Ectofuntus Dungeon Level 2"
                }, {
                        "3675,9888,1",
                        "Ectofuntus Dungeon Level 1"
                }, {
                        "3683,9888,0",
                        "Ectofuntus Bottom"
                }, {
                        "3792,3560,0",
                        "Dragontooth Island"
                }, {
                        "3166,3251,0",
                        "H. A. M. Hideout Entrance"
                }, {
                        "3149,9652,0",
                        "H. A. M. Hideout Dungeon"
                }, {
                        "3168,9572,0",
                        "Lumbridge Swamp Caves"
                }, {
                        "2825,10168,0",
                        "Between A Rock Quest Start"
                }, {
                        "2061,4955,3",
                        "Between A Rock Cutscenes"
                }, {
                        "2358,4959,0",
                        "Dondakan's Gold Mine 1"
                }, {
                        "2550,4956,0",
                        "Dondakan's Gold Mine 2"
                }, {
                        "2802,3672,0",
                        "Mountain Camp"
                }, {
                        "2787,10075,0",
                        "Mountain Daughter Dungeon"
                }, {
                        "3118,9644,0",
                        "Draynor Sewers"
                }, {
                        "2856,3812,0",
                        "Ice Path"
                }, {
                        "3570,3408,0",
                        "Graveyard"
                }, {
                        "3207,9377,0",
                        "Smoke Dungeon"
                }, {
                        "2737,5081,0",
                        "Shadow Dungeon"
                }, {
                        "3233,2898,0",
                        "Jaldraocht Pyramid Entrance"
                }, {
                        "3233,9315,0",
                        "Jaldraocht Pyramid Treasure Room"
                }, {
                        "3284,2806,0",
                        "Sophanem"
                }, {
                        "3288,2795,0",
                        "Jalsavrah Pyramid"
                }, {
                        "3261,2784,0",
                        "Menaphos"
                }, {
                        "3362,2998,0",
                        "Pollnivneach"
                }, {
                        "3277,9171,0",
                        "Klenter's Pyramid"
                }, {
                        "2270,4697,0",
                        "King Black Dragon's Lair New"
                }, {
                        "3490,3090,0",
                        "Uzer"
                }, {
                        "2722,4886,0",
                        "Uzer Dungeon"
                }, {
                        "3542,3310,0",
                        "Barrows Town"
                }, {
                        "3564,3288,0",
                        "Barrows"
                }, {
                        "3557,9703,3",
                        "Barrows Sarcophagus"
                }, {
                        "3552,9694,0",
                        "Barrows Maze Dungeon"
                }, {
                        "3257,9517,2",
                        "Tears Of Guthix"
                }, {
                        "2444,3051,0",
                        "Jiggig"
                }, {
                        "2481,3047,0",
                        "Zogre Flesh Eaters"
                }, {
                        "2477,9437,2",
                        "Zogre Flesh Eaters Dungeon"
                }, {
                        "2447,9417,0",
                        "Zogre Flesh Eaters Dungeon"
                }, {
                        "2481,9445,0",
                        "Zogre Flesh Eaters Dungeon"
                }, {
                        "2481,9445,0",
                        "Zogre Flesh Eaters Dungeon"
                }, {
                        "2847,10203,0",
                        "Keldagrim"
                }, {
                        "2035,4529,0",
                        "Giant Dwarf Cut Scene -1984 4480"
                }, {
                        "3222,9618,0",
                        "Dorgeshuun Tunnels"
                }, {
                        "2444,4956,0",
                        "Recruitment Drive Training Grounds"
                }, {
                        "3039,4834,0",
                        "Abyss"
                }, {
                        "3061,4983,1",
                        "Rogues' Den"
                }, {
                        "2335,3172,0",
                        "Lletya"
                }, {
                        "2044,4649,0",
                        "Mourners' Dungeon"
                }, {
                        "3682,9961,0",
                        "Port Phasmatys Brewery"
                }, {
                        "1862,4956,1",
                        "Forgettable Tale Of A Drunken Dwarf Rooms 1,3,5"
                }, {
                        "1868,4983,2",
                        "Forgettable Tale Of A Drunken Dwarf Room 2"
                }, {
                        "1898,4965,2",
                        "Forgettable Tale Of A Drunken Dwarf Room 4"
                }, {
                        "1862,4939,2",
                        "Forgettable Tale Of A Drunken Dwarf Room 6"
                }, {
                        "2610,4776,0",
                        "Freaky Forester"
                }, {
                        "2463,4782,0",
                        "Land Of The Frogs"
                }, {
                        "2526,4777,0",
                        "Evil Bob's Island"
                }, {
                        "1952,4764,0",
                        "Quiz"
                }, {
                        "2130,4913,0",
                        "Draynor Bank Robbery"
                }, {
                        "1940,4958,0",
                        "Blast Furnace"
                }, {
                        "2550,3758,0",
                        "Waterbirth Island"
                }, {
                        "1891,4527,2",
                        "A Tail Of Two Cats"
                }, {
                        "2480,5175,0",
                        "The Tzhaar"
                }, {
                        "2399,5177,0",
                        "Fight Pits"
                }, {
                        "2438,5168,0",
                        "Fight Caves Entrance"
                }, {
                        "2413,5117,0",
                        "Fight Caves"
                }, {
                        "1905,4639,0",
                        "Maze Of Light"
                }, {
                        "1862,4639,0",
                        "Death Altar Entrance"
                }, {
                        "2207,4836,0",
                        "Death Altar"
                }, {
                        "2162,5115,1",
                        "Braindeath Island"
                }, {
                        "3163,4821,0",
                        "Drill Demon"
                }, {
                        "2900,4449,0",
                        "Daggonath Kings"
                }, {
                        "2847,5064,0",
                        "Ratcatchers Mansion"
                }, {
                        "2896,5097,0",
                        "Varrock Rat Pits"
                }, {
                        "2660,9641,0",
                        "Ardougne Rat Pits"
                }, {
                        "1943,4702,0",
                        "Keldagrim Rat Pits"
                }, {
                        "2962,9650,0",
                        "Port Sarim Rat Pits"
                }, {
                        "3404,2915,0",
                        "Nardah"
                }, {
                        "3349,9536,0",
                        "Water Ravine Dungeon"
                }, {
                        "3373,9306,0",
                        "Crevice Dungeon"
                }, {
                        "3189,9758,0",
                        "Champions' Guild Dungeon"
                }, {
                        "3056,9555,0",
                        "Skeletal Wyverns"
                }, {
                        "2004,4426,1",
                        "Diango's Workshop"
                }, {
                        "3363,3305,0",
                        "Mage Training Arena"
                }, {
                        "3366,9623,2",
                        "Alchemists` Playground"
                }, {
                        "3382,9698,1",
                        "Telekentic Theatre Maze 1"
                }, {
                        "3378,9706,1",
                        "Telekentic Theatre Maze 2"
                }, {
                        "3354,9690,0",
                        "Telekentic Theatre Maze 3"
                }, {
                        "3355,9693,1",
                        "Telekentic Theatre Maze 4"
                }, {
                        "3362,9713,1",
                        "Telekentic Theatre Maze 5"
                }, {
                        "3379,9716,0",
                        "Telekentic Theatre Maze 6"
                }, {
                        "3359,9701,2",
                        "Telekentic Theatre Maze 7"
                }, {
                        "3368,9680,2",
                        "Telekentic Theatre Maze 8"
                }, {
                        "3374,9696,0",
                        "Telekentic Theatre Maze 9"
                }, {
                        "3336,9718,0",
                        "Telekentic Theatre Maze 10"
                }, {
                        "3363,9639,1",
                        "Creature Graveyard"
                }, {
                        "3363,9630,0",
                        "Enchanting Chamber"
                }, {
                        "2095,4428,0",
                        "Prison Pete"
                }, {
                        "3104,9310,0",
                        "Enakhra's Lament Dungeon"
                }, {
                        "3104,9310,1",
                        "Enakhra's Lament Dungeon"
                }, {
                        "3104,9310,2",
                        "Enakhra's Lament Dungeon"
                }, {
                        "3104,9310,3",
                        "Enakhra's Lament Dungeon"
                }, {
                        "3354,2830,0",
                        "Agility Pyramid"
                }, {
                        "3040,4692,1",
                        "Agility Pyramid Top Half"
                }, {
                        "1974,5002,0",
                        "Pious Pete/Candlelight"
                }, {
                        "1823,4835,0",
                        "Cabin Fever Ships"
                }, {
                        "2396,4379,0",
                        "Tanglefoot Lair"
                }, {
                        "3684,2953,0",
                        "Mos Le'harmless"
                }, {
                        "2675,5215,2",
                        "Killerwatt Plane"
                }, {
                        "1845,4492,0",
                        "Canoe Cut Scene"
                }, {
                        "2981,9915,0",
                        "Goblin Village Dungeon"
                }, {
                        "2981,9875,0",
                        "Goblin Village Dungeon Explode"
                }, {
                        "3077,9893,0",
                        "Basement Of Doom"
                }, {
                        "2963,9478,0",
                        "Mogre Camp"
                }, {
                        "1752,5236,0",
                        "Mole Lair"
                }, {
                        "1863,5336,0",
                        "Recipie For Disaster Feasts"
                }, {
                        "2580,4630,0",
                        "Recipie For Disaster Quiz"
                }, {
                        "1898,5356,2",
                        "Culinaromancer"
                }, {
                        "1928,5002,0",
                        "Gravedigger"
                }, {
                        "3485,3240,0",
                        "Burgh De Rott"
                }, {
                        "3490,9631,0",
                        "Myreque Hideout"
                }, {
                        "3490,9631,1",
                        "Myreque Hideout"
                }, {
                        "3414,9867,2",
                        "Drezel's Secret Library"
                }, {
                        "3461,9821,2",
                        "Ivandis' Tomb"
                }, {
                        "3309,3452,0",
                        "Rift Dungeon Entrance"
                }, {
                        "3015,5244,0",
                        "Rage"
                }, {
                        "3054,5244,0",
                        "Fear"
                }, {
                        "3060,5210,0",
                        "Confusion"
                }, {
                        "3085,5210,0",
                        "Hopelessness"
                }, {
                        "3021,5210,0",
                        "Hopelessness Bridge"
                }, {
                        "2971,5212,1",
                        "Tolna"
                }, {
                        "3298,9823,0",
                        "Tolna's Rift"
                }, {
                        "2667,2594,0",
                        "Pest Control Game"
                }, {
                        "2658,2649,0",
                        "Pest Control Bank"
                }, {
                        "2344,3650,0",
                        "Piscatoris Fishing Colony"
                }, {
                        "2509,3848,0",
                        "Miscellania Dungeon Entrance"
                }, {
                        "2510,10246,0",
                        "Miscellenia And Etceteria Dungeon"
                }, {
                        "3788,5709,0",
                        "Player Owned House"
                }, {
                        "1859,5058,0",
                        "Player Owned House"
                }, {
                        "1972,5046,0",
                        "Pinball Random"
                }, {
                        "1862,5137,0",
                        "Evil Twin Random"
                }, {
                        "3166,9623,0",
                        "H. A. M. Hideout Storerooms Entrance"
                }, {
                        "2568,5185,0",
                        "H. A. M. Hideout Storerooms"
                }, {
                        "2024,5088,0",
                        "Lumbridge Water Mill Cellar Dungeon"
                }, {
                        "2877,3546,0",
                        "Warriors' Guild"
                }, {
                        "1860,5244,0",
                        "Stronghold Of Security Dungeon Level 1"
                }, {
                        "2042,5245,0",
                        "Stronghold Of Security Dungeon Level 2"
                }, {
                        "2122,5251,0",
                        "Stronghold Of Security Dungeon Level 3"
                }, {
                        "2358,5215,0",
                        "Stronghold Of Security Dungeon Level 4"
                }, {
                        "3814,3022,0",
                        "Trouble Brewing"
                }, {
                        "3748,9374,0",
                        "Mos Le'harmless Caves"
                }, {
                        "2206,3810,0",
                        "Pirate's Cove"
                }, {
                        "2113,3915,0",
                        "Lunar Isle"
                }, {
                        "2329,10353,2",
                        "Lunar Isle Mine"
                }, {
                        "3103,4447,0",
                        "Berty"
                }, {
                        "1825,5165,2",
                        "Dream World"
                }, {
                        "1761,5087,2",
                        "Dream World"
                }, {
                        "1968,4450,3",
                        "Unknown"
                }, {
                        "1934,4420,3",
                        "Unknown"
                }, {
                        "1968,4420,2",
                        "Unknown"
                }, {
                        "1934,4450,2",
                        "Unknown"
                }, {
                        "1927,4477,0",
                        "Pyramid Plunder Room 1"
                }, {
                        "1954,4477,0",
                        "Pyramid Plunder Room 2"
                }, {
                        "1977,4471,0",
                        "Pyramid Plunder Room 3"
                }, {
                        "1927,4453,0",
                        "Pyramid Plunder Room 4"
                }, {
                        "1965,4444,0",
                        "Pyramid Plunder Room 5"
                }, {
                        "1927,4424,0",
                        "Pyramid Plunder Room 6"
                }, {
                        "1943,4421,0",
                        "Pyramid Plunder Room 7"
                }, {
                        "1974,4420,0",
                        "Pyramid Plunder Room 8"
                }, {
                        "3038,5346,0",
                        "Gorak Plane"
                }, {
                        "2254,4426,0",
                        "Fairy Resistance Hideout"
                }, {
                        "2075,4848,0",
                        "Cosmic Entity Plane"
                }, {
                        "3041,4531,0",
                        "Enchanted Valley"
                }, {
                        "1931,5043,0",
                        "Beekeeper Random"
                }, {
                        "2151,4962,0",
                        "The Eyes Of Glophrie Custcene"
                }, {
                        "3626,9618,0",
                        "Sanguinesti Order Base"
                }, {
                        "2387,4628,2",
                        "Meiyerditch Mine"
                }, {
                        "3637,9695,0",
                        "Meiyerditch Laboratory"
                }, {
                        "3206,3210,2",
                        "Lumbridge Castle Bank"
                }, {
                        "2716,3283,0",
                        "Witchaven"
                }, {
                        "1950,5155,2",
                        "Elemental Workshop II Dungeon"
                }, {
                        "1958,5150,0",
                        "Elemental Workshop II Dungeon"
                }, {
                        "1697,4515,0",
                        "Diango's Workshop"
                }, {
                        "1840,4918,0",
                        "Enlightened Journey Crashed Balloon"
                }, {
                        "1803,4893,0",
                        "Enlightened Journey Sunken Balloon"
                }, {
                        "2373,3611,0",
                        "Falconry"
                }, {
                        "2324,3497,0",
                        "Eagles' Peak"
                }, {
                        "2022,4982,3",
                        "Eagles' Peak Dungeon 1"
                }, {
                        "1957,4908,2",
                        "Eagles' Peak Dungeon 2"
                }, {
                        "1947,4868,2",
                        "Eagles' Peak Dungeon 3"
                }, {
                        "1974,4908,2",
                        "Eagles' Peak Dungeon Feather"
                }, {
                        "2728,10213,0",
                        "Polar Eagle Eyrie"
                }, {
                        "2526,9318,0",
                        "Feldip Eagle Eyrie"
                }, {
                        "3422,9568,0",
                        "Uzer Eagle Eyrie"
                }, {
                        "3168,5321,0",
                        "Gublinch Lair"
                }, {
                        "2532,3571,0",
                        "Barbarian Assault"
                }, {
                        "2593,5254,0",
                        "Barbarian Assault Dungeon"
                }, {
                        "1886,5454,0",
                        "Barbarian Assault Waves 1 - 9 Dungeon"
                }, {
                        "1886,5390,0",
                        "Barbarian Assault Queen Wave Dungeon"
                }, {
                        "2658,3988,1",
                        "Iceberg"
                }, {
                        "2658,10386,0",
                        "Kgp Base"
                }, {
                        "3322,9241,0",
                        "Sophanem Dungeon"
                }, {
                        "3256,9225,0",
                        "Sophanem Dungeon"
                }, {
                        "3268,9228,2",
                        "Sophanem Dungeon"
                }, {
                        "2765,5131,0",
                        "Sophanem Bank Destroyed"
                }, {
                        "2799,5160,0",
                        "Sophanem Bank"
                }, {
                        "3424,9660,0",
                        "Tarn's Lair Entrance"
                }, {
                        "3186,4624,0",
                        "Lair Of Tarn Razorlor"
                }, {
                        "3149,4654,0",
                        "Lair Of Tarn Razorlor"
                }, {
                        "2336,3803,0",
                        "Neitiznot"
                }, {
                        "2416,3801,0",
                        "Jatizso"
                }, {
                        "2406,10190,0",
                        "Jatizso Mine"
                }, {
                        "2394,10300,1",
                        "Ice Troll Caves"
                }, {
                        "2909,5468,0",
                        "Sorceress's Garden"
                }, {
                        "2649,3225,0",
                        "Tower Of Life"
                }, {
                        "3038,4376,0",
                        "Tower Of Life Dungeon"
                }, {
                        "3786,2823,0",
                        "Harmony Island"
                }, {
                        "3786,9225,0",
                        "Harmony Island Dungeon"
                }, {
                        "3786,9254,0",
                        "Great Brain Robbery Quest Dungeon"
                }, {
                        "3828,9254,1",
                        "Great Brain Robbery Quest Dungeon"
                }, {
                        "3179,5191,0",
                        "Tunnel Of Chaos"
                }, {
                        "2520,5607,0",
                        "Train Station"
                }, {
                        "3171,9569,0",
                        "Another Slice Of H. A. M. Entrance"
                }, {
                        "2397,5556,0",
                        "Another Slice Of H. A. M. Dungeon"
                }, {
                        "2488,5536,0",
                        "Train"
                }, {
                        "2435,5535,0",
                        "Train"
                }, {
                        "2691,10124,0",
                        "Brine Rat Cavern"
                }, {
                        "2839,9388,0",
                        "Shilo Village Mining Site Dungeon"
                }, {
                        "1824,5152,2",
                        "Cyrisus' Dream"
                }, {
                        "1759,4958,0",
                        "Varrock Museum Basement"
                }, {
                        "2279,5554,3",
                        "Mouse Hole"
                }, {
                        "2142,5525,3",
                        "Glod's Location"
                }, {
                        "2592,4317,0",
                        "Puro-Puro"
                }, {
                        "1886,5022,0",
                        "Surprise Exam Random"
                }, {
                        "1904,4283,0",
                        "King's Ransom Jail"
                }, {
                        "1763,5365,1",
                        "Ancient Cavern"
                }, {
                        "2914,3746,0",
                        "God Wars Dungeon Entrance"
                }, {
                        "2881,5310,2",
                        "God Wars Dungeon"
                }, {
                        "2839,5295,2",
                        "Armadyl's Eyrie"
                }, {
                        "2863,5354,2",
                        "Bandos's Stronghold"
                }, {
                        "2910,5265,0",
                        "Saradomin's Encampment"
                }, {
                        "2925,5333,2",
                        "Zamorak's Fortress"
                }, {
                        "2321,3100,0",
                        "Poison Waste Sewers Entrance"
                }, {
                        "1985,4174,0",
                        "Poison Waste Sewers"
                }, {
                        "3271,4861,0",
                        "Ourania Cave"
                }, {
                        "3313,4811,0",
                        "Zmi Altar/Ourania Runecrafting Altar"
                }, {
                        "1696,4825,0",
                        "Mansion Of Death"
                }, {
                        "1644,4848,0",
                        "Playground Of Solitude"
                }, {
                        "3164,3487,0",
                        "Grand Exchange"
                }, {
                        "3231,5091,0",
                        "Tournaments Dungeon"
                }, {
                        "3282,5075,0",
                        "Tournaments Dungeon Arena"
                }, {
                        "2585,4193,0",
                        "Yu'biusk Cutscene"
                }, {
                        "2784,4252,0",
                        "Goblin Temple Dungeon"
                }, {
                        "2462,4254,0",
                        "Crypt"
                }, {
                        "2228,4245,1",
                        "Yu'biusk"
                }, {
                        "3428,2783,0",
                        "Ruins Of Ullek"
                }, {
                        "3448,9252,1",
                        "Scabarite Hive Dungeon"
                }, {
                        "2209,5348,0",
                        "Summoning"
                }, {
                        "3248,5489,0",
                        "Chaos Tunnels"
                }, {
                        "1638,4709,0",
                        "Vinesweeper"
                }, {
                        "3100,5536,0",
                        "Bork's Chamber"
                }, {
                        "3086,4247,0",
                        "Stronghold Of Player Safety Jail"
                }, {
                        "1946,5144,0",
                        "Stronghold Of Player Safety Secure Sector"
                }, {
                        "3174,4273,2",
                        "Stronghold Of Player Safety Middle Level"
                }, {
                        "3171,4273,3",
                        "Stronghold Of Player Safety Upper Level"
                }, {
                        "3142,4272,1",
                        "Stronghold Of Player Safety Lower Level"
                }, {
                        "1778,5740,0",
                        "Fist Of Guthix Cutscene"
                }, {
                        "1677,5599,0",
                        "Fist Of Guthix Lobby"
                }, {
                        "1653,5601,0",
                        "Fist Of Guthix Waiting Room"
                }, {
                        "1663,5696,0",
                        "Fist Of Guthix Game Arena"
                }, {
                        "3422,9965,0",
                        "Columbarium"
                }, {
                        "3561,9774,0",
                        "Blood Altar Entrance"
                }, {
                        "2468,4889,1",
                        "Blood Altar"
                }, {
                        "2512,5204,0",
                        "Toktz-Ket-Dill"
                }, {
                        "3357,9352,0",
                        "Pollnivneach Slayer Dungeon"
                }, {
                        "3795,9938,1",
                        "Dragontooth Shipwreck"
                }, {
                        "1632,5024,0",
                        "Battle Of The Archipelago"
                }, {
                        "3169,4939,0",
                        "Lumbridge Mine"
                }, {
                        "2945,4286,0",
                        "Spirit World"
                }, {
                        "2863,4267,0",
                        "Spirit World"
                }, {
                        "2835,4317,0",
                        "Spirit World Dungeon"
                }, {
                        "2723,4324,0",
                        "Wilderness Spirit Realm 1"
                }, {
                        "2779,4326,0",
                        "Wilderness Spirit Realm 2"
                }, {
                        "2784,4384,0",
                        "Wilderness Spirit Realm 3"
                }, {
                        "2848,4382,0",
                        "Wilderness Spirit Realm 4"
                }, {
                        "2834,4259,0",
                        "Wilderness Spirit Realm 5"
                }, {
                        "2933,4292,0",
                        "Wilderness Spirit Realm 6"
                }, {
                        "3296,4187,0",
                        "Wilderness Spirit Realm 7"
                }, {
                        "3230,4186,0",
                        "Wilderness Spirit Realm 8"
                }, {
                        "1696,5460,2",
                        "Runecrafting Guild"
                }, {
                        "2988,4376,0",
                        "Corporeal Beast Cave 1"
                }, {
                        "2988,4376,1",
                        "Corporeal Beast Cave 2"
                }, {
                        "2988,4376,2",
                        "Corporeal Beast Cave 3"
                }, {
                        "3548,5599,0",
                        "Circus"
                }, {
                        "3041,9903,0",
                        "Hall Of Camdozaal"
                }, {
                        "1888,5546,0",
                        "Defenders Of Varrock Cutscene"
                }, {
                        "3241,9991,0",
                        "Chaos Temple Dungeon"
                }, {
                        "3165,4515,0",
                        "Hetty's Basement"
                }, {
                        "3221,4522,0",
                        "Betty's Basement"
                }, {
                        "3290,4514,0",
                        "Swept Away Puzzle 1"
                }, {
                        "3232,4448,0",
                        "Swept Away Puzzle 2"
                }, {
                        "2530,5871,0",
                        "Ancient Guthix Temple"
                }, {
                        "2600,5727,0",
                        "Tormented Demons"
                }, {
                        "1921,5697,0",
                        "Stealing Creation"
                }, {
                        "3431,4586,0",
                        "Jack's Hideout"
                }, {
                        "2294,3626,0",
                        "Phoenix Lair Entrance"
                }, {
                        "3543,5218,0",
                        "Phoenix Lair Cave 1"
                }, {
                        "3517,5224,0",
                        "Phoenix Lair Cave 2"
                }, {
                        "3476,5186,0",
                        "Phoenix Lair Cave 3"
                }, {
                        "3493,5186,0",
                        "Phoenix Lair Cave 4"
                }, {
                        "3461,5218,0",
                        "Phoenix Lair Cave 5"
                }, {
                        "3535,5186,0",
                        "Phoenix Lair Cave 6"
                }, {
                        "3566,5224,0",
                        "Phoenix Eggs"
                }, {
                        "2319,4241,0",
                        "Bandos' Throne Room"
                }, {
                        "2332,4329,0",
                        "Sigmund's Hideout"
                }, {
                        "3414,5541,0",
                        "Muspah Cave"
                }, {
                        "3486,5543,0",
                        "Muspah Cave"
                }, {
                        "2885,3867,0",
                        "Mahjarrat Ritual Site"
                }, {
                        "3607,5784,0",
                        "Red Raktuber Surfaced"
                }, {
                        "3545,5800,0",
                        "Inside The Red Raktuber"
                }, {
                        "3487,9235,0",
                        "Tomb Of Catolax"
                }, {
                        "2720,9971,0",
                        "Aquantie"
                }, {
                        "3367,3513,0",
                        "Skeletal Horror Entrance"
                }, {
                        "1520,4704,0",
                        "Chaos Dwarf Battlefield"
                }, {
                        "3290,4936,0",
                        "Unstable Foundations Dungeon"
                }, {
                        "3653,5115,0",
                        "Living Rock Caverns"
                }, {
                        "2026,4571,2",
                        "Maze Of Pain"
                }, {
                        "3182,5708,0",
                        "Altar Of Zaros"
                }, {
                        "1656,5258,0",
                        "Kuradal's Dungeon"
                }, {
                        "4448,5856,1",
                        "Silas's Dream"
                }, {
                        "4382,5919,0",
                        "Fremennik Spiritual Realm"
                }
                },

                new Object[]{
                        "Location",
                        "Name"
                });

        public DefaultTableModel getModel() {
                return model;
        }
}