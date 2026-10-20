package Proyecto;

import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Datos genéticos de especies: grupos huevo, proporción de sexos y preevolución.
 * Catálogo generado desde los datos de especies de Pokémon Showdown; solo usa especies
 * del juego propio. No requiere acceso a Internet en tiempo de ejecución.
 */
public final class GeneticaPokemon {
    private GeneticaPokemon() { }

    private static final String DATOS = """
            Bulbasaur|0.875|0.125|Monster,Grass|
            Ivysaur|0.875|0.125|Monster,Grass|Bulbasaur
            Venusaur|0.875|0.125|Monster,Grass|Ivysaur
            Charmander|0.875|0.125|Monster,Dragon|
            Charmeleon|0.875|0.125|Monster,Dragon|Charmander
            Charizard|0.875|0.125|Monster,Dragon|Charmeleon
            Squirtle|0.875|0.125|Monster,Water 1|
            Wartortle|0.875|0.125|Monster,Water 1|Squirtle
            Blastoise|0.875|0.125|Monster,Water 1|Wartortle
            Caterpie|-1|-1|Bug|
            Metapod|-1|-1|Bug|Caterpie
            Butterfree|-1|-1|Bug|Metapod
            Weedle|-1|-1|Bug|
            Kakuna|-1|-1|Bug|Weedle
            Beedrill|-1|-1|Bug|Kakuna
            Pidgey|-1|-1|Flying|
            Pidgeotto|-1|-1|Flying|Pidgey
            Pidgeot|-1|-1|Flying|Pidgeotto
            Rattata|-1|-1|Field|
            Raticate|-1|-1|Field|Rattata
            Spearow|-1|-1|Flying|
            Fearow|-1|-1|Flying|Spearow
            Ekans|-1|-1|Field,Dragon|
            Arbok|-1|-1|Field,Dragon|Ekans
            Pikachu|-1|-1|Field,Fairy|Pichu
            Raichu|-1|-1|Field,Fairy|Pikachu
            Sandshrew|-1|-1|Field|
            Sandslash|-1|-1|Field|Sandshrew
            Nidoran♀|-1|-1|Monster,Field|
            Nidorina|-1|-1|Undiscovered|Nidoran-F
            Nidoqueen|-1|-1|Undiscovered|Nidorina
            Nidoran♂|-1|-1|Monster,Field|
            Nidorino|-1|-1|Monster,Field|Nidoran-M
            Nidoking|-1|-1|Monster,Field|Nidorino
            Clefairy|0.25|0.75|Fairy|Cleffa
            Clefable|0.25|0.75|Fairy|Clefairy
            Vulpix|0.25|0.75|Field|
            Ninetales|0.25|0.75|Field|Vulpix
            Jigglypuff|0.25|0.75|Fairy|Igglybuff
            Wigglytuff|0.25|0.75|Fairy|Jigglypuff
            Zubat|-1|-1|Flying|
            Golbat|-1|-1|Flying|Zubat
            Oddish|-1|-1|Grass|
            Gloom|-1|-1|Grass|Oddish
            Vileplume|-1|-1|Grass|Gloom
            Paras|-1|-1|Bug,Grass|
            Parasect|-1|-1|Bug,Grass|Paras
            Venonat|-1|-1|Bug|
            Venomoth|-1|-1|Bug|Venonat
            Diglett|-1|-1|Field|
            Dugtrio|-1|-1|Field|Diglett
            Meowth|-1|-1|Field|
            Persian|-1|-1|Field|Meowth
            Psyduck|-1|-1|Water 1,Field|
            Golduck|-1|-1|Water 1,Field|Psyduck
            Mankey|-1|-1|Field|
            Primeape|-1|-1|Field|Mankey
            Growlithe|0.75|0.25|Field|
            Arcanine|0.75|0.25|Field|Growlithe
            Poliwag|-1|-1|Water 1|
            Poliwhirl|-1|-1|Water 1|Poliwag
            Poliwrath|-1|-1|Water 1|Poliwhirl
            Abra|0.75|0.25|Human-Like|
            Kadabra|0.75|0.25|Human-Like|Abra
            Alakazam|0.75|0.25|Human-Like|Kadabra
            Machop|0.75|0.25|Human-Like|
            Machoke|0.75|0.25|Human-Like|Machop
            Machamp|0.75|0.25|Human-Like|Machoke
            Bellsprout|-1|-1|Grass|
            Weepinbell|-1|-1|Grass|Bellsprout
            Victreebel|-1|-1|Grass|Weepinbell
            Tentacool|-1|-1|Water 3|
            Tentacruel|-1|-1|Water 3|Tentacool
            Geodude|-1|-1|Mineral|
            Graveler|-1|-1|Mineral|Geodude
            Golem|-1|-1|Mineral|Graveler
            Ponyta|-1|-1|Field|
            Rapidash|-1|-1|Field|Ponyta
            Slowpoke|-1|-1|Monster,Water 1|
            Slowbro|-1|-1|Monster,Water 1|Slowpoke
            Magnemite|-1|-1|Mineral|
            Magneton|-1|-1|Mineral|Magnemite
            Farfetch'd|-1|-1|Flying,Field|
            Doduo|-1|-1|Flying|
            Dodrio|-1|-1|Flying|Doduo
            Seel|-1|-1|Water 1,Field|
            Dewgong|-1|-1|Water 1,Field|Seel
            Grimer|-1|-1|Amorphous|
            Muk|-1|-1|Amorphous|Grimer
            Shellder|-1|-1|Water 3|
            Cloyster|-1|-1|Water 3|Shellder
            Gastly|-1|-1|Amorphous|
            Haunter|-1|-1|Amorphous|Gastly
            Gengar|-1|-1|Amorphous|Haunter
            Onix|-1|-1|Mineral|
            Drowzee|-1|-1|Human-Like|
            Hypno|-1|-1|Human-Like|Drowzee
            Krabby|-1|-1|Water 3|
            Kingler|-1|-1|Water 3|Krabby
            Voltorb|-1|-1|Mineral|
            Electrode|-1|-1|Mineral|Voltorb
            Exeggcute|-1|-1|Grass|
            Exeggutor|-1|-1|Grass|Exeggcute
            Cubone|-1|-1|Monster|
            Marowak|-1|-1|Monster|Cubone
            Hitmonlee|-1|-1|Human-Like|Tyrogue
            Hitmonchan|-1|-1|Human-Like|Tyrogue
            Lickitung|-1|-1|Monster|
            Koffing|-1|-1|Amorphous|
            Weezing|-1|-1|Amorphous|Koffing
            Rhyhorn|-1|-1|Monster,Field|
            Rhydon|-1|-1|Monster,Field|Rhyhorn
            Chansey|-1|-1|Fairy|Happiny
            Tangela|-1|-1|Grass|
            Kangaskhan|-1|-1|Monster|
            Horsea|-1|-1|Water 1,Dragon|
            Seadra|-1|-1|Water 1,Dragon|Horsea
            Goldeen|-1|-1|Water 2|
            Seaking|-1|-1|Water 2|Goldeen
            Staryu|-1|-1|Water 3|
            Starmie|-1|-1|Water 3|Staryu
            Mr. Mime|-1|-1|Human-Like|Mime Jr.
            Scyther|-1|-1|Bug|
            Jynx|-1|-1|Human-Like|Smoochum
            Electabuzz|0.75|0.25|Human-Like|Elekid
            Magmar|0.75|0.25|Human-Like|Magby
            Pinsir|-1|-1|Bug|
            Tauros|-1|-1|Field|
            Magikarp|-1|-1|Water 2,Dragon|
            Gyarados|-1|-1|Water 2,Dragon|Magikarp
            Lapras|-1|-1|Monster,Water 1|
            Ditto|-1|-1|Ditto|
            Eevee|0.875|0.125|Field|
            Vaporeon|0.875|0.125|Field|Eevee
            Jolteon|0.875|0.125|Field|Eevee
            Flareon|0.875|0.125|Field|Eevee
            Porygon|-1|-1|Mineral|
            Omanyte|0.875|0.125|Water 1,Water 3|
            Omastar|0.875|0.125|Water 1,Water 3|Omanyte
            Kabuto|0.875|0.125|Water 1,Water 3|
            Kabutops|0.875|0.125|Water 1,Water 3|Kabuto
            Aerodactyl|0.875|0.125|Flying|
            Snorlax|0.875|0.125|Monster|Munchlax
            Articuno|-1|-1|Undiscovered|
            Zapdos|-1|-1|Undiscovered|
            Moltres|-1|-1|Undiscovered|
            Dratini|-1|-1|Water 1,Dragon|
            Dragonair|-1|-1|Water 1,Dragon|Dratini
            Dragonite|-1|-1|Water 1,Dragon|Dragonair
            Mewtwo|-1|-1|Undiscovered|
            Mew|-1|-1|Undiscovered|
            Chikorita|0.875|0.125|Monster,Grass|
            Bayleef|0.875|0.125|Monster,Grass|Chikorita
            Meganium|0.875|0.125|Monster,Grass|Bayleef
            Cyndaquil|0.875|0.125|Field|
            Quilava|0.875|0.125|Field|Cyndaquil
            Typhlosion|0.875|0.125|Field|Quilava
            Totodile|0.875|0.125|Monster,Water 1|
            Croconaw|0.875|0.125|Monster,Water 1|Totodile
            Feraligatr|0.875|0.125|Monster,Water 1|Croconaw
            Sentret|-1|-1|Field|
            Furret|-1|-1|Field|Sentret
            Hoothoot|-1|-1|Flying|
            Noctowl|-1|-1|Flying|Hoothoot
            Ledyba|-1|-1|Bug|
            Ledian|-1|-1|Bug|Ledyba
            Spinarak|-1|-1|Bug|
            Ariados|-1|-1|Bug|Spinarak
            Crobat|-1|-1|Flying|Golbat
            Chinchou|-1|-1|Water 2|
            Lanturn|-1|-1|Water 2|Chinchou
            Pichu|-1|-1|Undiscovered|
            Cleffa|0.25|0.75|Undiscovered|
            Igglybuff|0.25|0.75|Undiscovered|
            Togepi|0.875|0.125|Undiscovered|
            Togetic|0.875|0.125|Flying,Fairy|Togepi
            Natu|-1|-1|Flying|
            Xatu|-1|-1|Flying|Natu
            Mareep|-1|-1|Monster,Field|
            Flaaffy|-1|-1|Monster,Field|Mareep
            Ampharos|-1|-1|Monster,Field|Flaaffy
            Bellossom|-1|-1|Grass|Gloom
            Marill|-1|-1|Water 1,Fairy|Azurill
            Azumarill|-1|-1|Water 1,Fairy|Marill
            Sudowoodo|-1|-1|Mineral|Bonsly
            Politoed|-1|-1|Water 1|Poliwhirl
            Hoppip|-1|-1|Fairy,Grass|
            Skiploom|-1|-1|Fairy,Grass|Hoppip
            Jumpluff|-1|-1|Fairy,Grass|Skiploom
            Aipom|-1|-1|Field|
            Sunkern|-1|-1|Grass|
            Sunflora|-1|-1|Grass|Sunkern
            Yanma|-1|-1|Bug|
            Wooper|-1|-1|Water 1,Field|
            Quagsire|-1|-1|Water 1,Field|Wooper
            Espeon|0.875|0.125|Field|Eevee
            Umbreon|0.875|0.125|Field|Eevee
            Murkrow|-1|-1|Flying|
            Slowking|-1|-1|Monster,Water 1|Slowpoke
            Misdreavus|-1|-1|Amorphous|
            Unown|-1|-1|Undiscovered|
            Wobbuffet|-1|-1|Amorphous|Wynaut
            Girafarig|-1|-1|Field|
            Pineco|-1|-1|Bug|
            Forretress|-1|-1|Bug|Pineco
            Dunsparce|-1|-1|Field|
            Gligar|-1|-1|Bug|
            Steelix|-1|-1|Mineral|Onix
            Snubbull|0.25|0.75|Field,Fairy|
            Granbull|0.25|0.75|Field,Fairy|Snubbull
            Qwilfish|-1|-1|Water 2|
            Scizor|-1|-1|Bug|Scyther
            Shuckle|-1|-1|Bug|
            Heracross|-1|-1|Bug|
            Sneasel|-1|-1|Field|
            Teddiursa|-1|-1|Field|
            Ursaring|-1|-1|Field|Teddiursa
            Slugma|-1|-1|Amorphous|
            Magcargo|-1|-1|Amorphous|Slugma
            Swinub|-1|-1|Field|
            Piloswine|-1|-1|Field|Swinub
            Corsola|0.25|0.75|Water 1,Water 3|
            Remoraid|-1|-1|Water 1,Water 2|
            Octillery|-1|-1|Water 1,Water 2|Remoraid
            Delibird|-1|-1|Water 1,Field|
            Mantine|-1|-1|Water 1|Mantyke
            Skarmory|-1|-1|Flying|
            Houndour|-1|-1|Field|
            Houndoom|-1|-1|Field|Houndour
            Kingdra|-1|-1|Water 1,Dragon|Seadra
            Phanpy|-1|-1|Field|
            Donphan|-1|-1|Field|Phanpy
            Porygon2|-1|-1|Mineral|Porygon
            Stantler|-1|-1|Field|
            Smeargle|-1|-1|Field|
            Tyrogue|-1|-1|Undiscovered|
            Hitmontop|-1|-1|Human-Like|Tyrogue
            Smoochum|-1|-1|Undiscovered|
            Elekid|0.75|0.25|Undiscovered|
            Magby|0.75|0.25|Undiscovered|
            Miltank|-1|-1|Field|
            Blissey|-1|-1|Fairy|Chansey
            Raikou|-1|-1|Undiscovered|
            Entei|-1|-1|Undiscovered|
            Suicune|-1|-1|Undiscovered|
            Larvitar|-1|-1|Monster|
            Pupitar|-1|-1|Monster|Larvitar
            Tyranitar|-1|-1|Monster|Pupitar
            Lugia|-1|-1|Undiscovered|
            Ho-oh|-1|-1|Undiscovered|
            Celebi|-1|-1|Undiscovered|
            Treecko|0.875|0.125|Monster,Dragon|
            Grovyle|0.875|0.125|Monster,Dragon|Treecko
            Sceptile|0.875|0.125|Monster,Dragon|Grovyle
            Torchic|0.875|0.125|Field|
            Combusken|0.875|0.125|Field|Torchic
            Blaziken|0.875|0.125|Field|Combusken
            Mudkip|0.875|0.125|Monster,Water 1|
            Marshtomp|0.875|0.125|Monster,Water 1|Mudkip
            Swampert|0.875|0.125|Monster,Water 1|Marshtomp
            Poochyena|-1|-1|Field|
            Mightyena|-1|-1|Field|Poochyena
            Zigzagoon|-1|-1|Field|
            Linoone|-1|-1|Field|Zigzagoon
            Wurmple|-1|-1|Bug|
            Silcoon|-1|-1|Bug|Wurmple
            Beautifly|-1|-1|Bug|Silcoon
            Cascoon|-1|-1|Bug|Wurmple
            Dustox|-1|-1|Bug|Cascoon
            Lotad|-1|-1|Water 1,Grass|
            Lombre|-1|-1|Water 1,Grass|Lotad
            Ludicolo|-1|-1|Water 1,Grass|Lombre
            Seedot|-1|-1|Field,Grass|
            Nuzleaf|-1|-1|Field,Grass|Seedot
            Shiftry|-1|-1|Field,Grass|Nuzleaf
            Taillow|-1|-1|Flying|
            Swellow|-1|-1|Flying|Taillow
            Wingull|-1|-1|Water 1,Flying|
            Pelipper|-1|-1|Water 1,Flying|Wingull
            Ralts|-1|-1|Human-Like,Amorphous|
            Kirlia|-1|-1|Human-Like,Amorphous|Ralts
            Gardevoir|-1|-1|Human-Like,Amorphous|Kirlia
            Surskit|-1|-1|Water 1,Bug|
            Masquerain|-1|-1|Water 1,Bug|Surskit
            Shroomish|-1|-1|Fairy,Grass|
            Breloom|-1|-1|Fairy,Grass|Shroomish
            Slakoth|-1|-1|Field|
            Vigoroth|-1|-1|Field|Slakoth
            Slaking|-1|-1|Field|Vigoroth
            Nincada|-1|-1|Bug|
            Ninjask|-1|-1|Bug|Nincada
            Shedinja|-1|-1|Mineral|Nincada
            Whismur|-1|-1|Monster,Field|
            Loudred|-1|-1|Monster,Field|Whismur
            Exploud|-1|-1|Monster,Field|Loudred
            Makuhita|0.75|0.25|Human-Like|
            Hariyama|0.75|0.25|Human-Like|Makuhita
            Azurill|0.25|0.75|Undiscovered|
            Nosepass|-1|-1|Mineral|
            Skitty|0.25|0.75|Field,Fairy|
            Delcatty|0.25|0.75|Field,Fairy|Skitty
            Sableye|-1|-1|Human-Like|
            Mawile|-1|-1|Field,Fairy|
            Aron|-1|-1|Monster|
            Lairon|-1|-1|Monster|Aron
            Aggron|-1|-1|Monster|Lairon
            Meditite|-1|-1|Human-Like|
            Medicham|-1|-1|Human-Like|Meditite
            Electrike|-1|-1|Field|
            Manectric|-1|-1|Field|Electrike
            Plusle|-1|-1|Fairy|
            Minun|-1|-1|Fairy|
            Volbeat|-1|-1|Bug,Human-Like|
            Illumise|-1|-1|Bug,Human-Like|
            Roselia|-1|-1|Fairy,Grass|Budew
            Gulpin|-1|-1|Amorphous|
            Swalot|-1|-1|Amorphous|Gulpin
            Carvanha|-1|-1|Water 2|
            Sharpedo|-1|-1|Water 2|Carvanha
            Wailmer|-1|-1|Field,Water 2|
            Wailord|-1|-1|Field,Water 2|Wailmer
            Numel|-1|-1|Field|
            Camerupt|-1|-1|Field|Numel
            Torkoal|-1|-1|Field|
            Spoink|-1|-1|Field|
            Grumpig|-1|-1|Field|Spoink
            Spinda|-1|-1|Field,Human-Like|
            Trapinch|-1|-1|Bug,Dragon|
            Vibrava|-1|-1|Bug,Dragon|Trapinch
            Flygon|-1|-1|Bug,Dragon|Vibrava
            Cacnea|-1|-1|Grass,Human-Like|
            Cacturne|-1|-1|Grass,Human-Like|Cacnea
            Swablu|-1|-1|Flying,Dragon|
            Altaria|-1|-1|Flying,Dragon|Swablu
            Zangoose|-1|-1|Field|
            Seviper|-1|-1|Field,Dragon|
            Lunatone|-1|-1|Mineral|
            Solrock|-1|-1|Mineral|
            Barboach|-1|-1|Water 2|
            Whiscash|-1|-1|Water 2|Barboach
            Corphish|-1|-1|Water 1,Water 3|
            Crawdaunt|-1|-1|Water 1,Water 3|Corphish
            Baltoy|-1|-1|Mineral|
            Claydol|-1|-1|Mineral|Baltoy
            Lileep|0.875|0.125|Water 3|
            Cradily|0.875|0.125|Water 3|Lileep
            Anorith|0.875|0.125|Water 3|
            Armaldo|0.875|0.125|Water 3|Anorith
            Feebas|-1|-1|Water 1,Dragon|
            Milotic|-1|-1|Water 1,Dragon|Feebas
            Castform|-1|-1|Fairy,Amorphous|
            Kecleon|-1|-1|Field|
            Shuppet|-1|-1|Amorphous|
            Banette|-1|-1|Amorphous|Shuppet
            Duskull|-1|-1|Amorphous|
            Dusclops|-1|-1|Amorphous|Duskull
            Tropius|-1|-1|Monster,Grass|
            Chimecho|-1|-1|Amorphous|Chingling
            Absol|-1|-1|Field|
            Wynaut|-1|-1|Undiscovered|
            Snorunt|-1|-1|Fairy,Mineral|
            Glalie|-1|-1|Fairy,Mineral|Snorunt
            Spheal|-1|-1|Water 1,Field|
            Sealeo|-1|-1|Water 1,Field|Spheal
            Walrein|-1|-1|Water 1,Field|Sealeo
            Clamperl|-1|-1|Water 1|
            Huntail|-1|-1|Water 1|Clamperl
            Gorebyss|-1|-1|Water 1|Clamperl
            Relicanth|0.875|0.125|Water 1,Water 2|
            Luvdisc|0.25|0.75|Water 2|
            Bagon|-1|-1|Dragon|
            Shelgon|-1|-1|Dragon|Bagon
            Salamence|-1|-1|Dragon|Shelgon
            Beldum|-1|-1|Mineral|
            Metang|-1|-1|Mineral|Beldum
            Metagross|-1|-1|Mineral|Metang
            Regirock|-1|-1|Undiscovered|
            Regice|-1|-1|Undiscovered|
            Registeel|-1|-1|Undiscovered|
            Latias|-1|-1|Undiscovered|
            Latios|-1|-1|Undiscovered|
            Kyogre|-1|-1|Undiscovered|
            Groudon|-1|-1|Undiscovered|
            Rayquaza|-1|-1|Undiscovered|
            Jirachi|-1|-1|Undiscovered|
            Deoxys|-1|-1|Undiscovered|
            Turtwig|0.875|0.125|Monster,Grass|
            Grotle|0.875|0.125|Monster,Grass|Turtwig
            Torterra|0.875|0.125|Monster,Grass|Grotle
            Chimchar|0.875|0.125|Field,Human-Like|
            Monferno|0.875|0.125|Field,Human-Like|Chimchar
            Infernape|0.875|0.125|Field,Human-Like|Monferno
            Piplup|0.875|0.125|Water 1,Field|
            Prinplup|0.875|0.125|Water 1,Field|Piplup
            Empoleon|0.875|0.125|Water 1,Field|Prinplup
            Starly|-1|-1|Flying|
            Staravia|-1|-1|Flying|Starly
            Staraptor|-1|-1|Flying|Staravia
            Bidoof|-1|-1|Water 1,Field|
            Bibarel|-1|-1|Water 1,Field|Bidoof
            Kricketot|-1|-1|Bug|
            Kricketune|-1|-1|Bug|Kricketot
            Shinx|-1|-1|Field|
            Luxio|-1|-1|Field|Shinx
            Luxray|-1|-1|Field|Luxio
            Budew|-1|-1|Undiscovered|
            Roserade|-1|-1|Fairy,Grass|Roselia
            Cranidos|0.875|0.125|Monster|
            Rampardos|0.875|0.125|Monster|Cranidos
            Shieldon|0.875|0.125|Monster|
            Bastiodon|0.875|0.125|Monster|Shieldon
            Burmy|-1|-1|Bug|
            Wormadam|-1|-1|Bug|Burmy
            Mothim|-1|-1|Bug|Burmy
            Combee|0.875|0.125|Bug|
            Vespiquen|-1|-1|Bug|Combee
            Pachirisu|-1|-1|Field,Fairy|
            Buizel|-1|-1|Water 1,Field|
            Floatzel|-1|-1|Water 1,Field|Buizel
            Cherubi|-1|-1|Fairy,Grass|
            Cherrim|-1|-1|Fairy,Grass|Cherubi
            Shellos|-1|-1|Water 1,Amorphous|
            Gastrodon|-1|-1|Water 1,Amorphous|Shellos
            Ambipom|-1|-1|Field|Aipom
            Drifloon|-1|-1|Amorphous|
            Drifblim|-1|-1|Amorphous|Drifloon
            Buneary|-1|-1|Field,Human-Like|
            Lopunny|-1|-1|Field,Human-Like|Buneary
            Mismagius|-1|-1|Amorphous|Misdreavus
            Honchkrow|-1|-1|Flying|Murkrow
            Glameow|0.25|0.75|Field|
            Purugly|0.25|0.75|Field|Glameow
            Chingling|-1|-1|Undiscovered|
            Stunky|-1|-1|Field|
            Skuntank|-1|-1|Field|Stunky
            Bronzor|-1|-1|Mineral|
            Bronzong|-1|-1|Mineral|Bronzor
            Bonsly|-1|-1|Undiscovered|
            Mime Jr.|-1|-1|Undiscovered|
            Happiny|-1|-1|Undiscovered|
            Chatot|-1|-1|Flying|
            Spiritomb|-1|-1|Amorphous|
            Gible|-1|-1|Monster,Dragon|
            Gabite|-1|-1|Monster,Dragon|Gible
            Garchomp|-1|-1|Monster,Dragon|Gabite
            Munchlax|0.875|0.125|Undiscovered|
            Riolu|0.875|0.125|Undiscovered|
            Lucario|0.875|0.125|Field,Human-Like|Riolu
            Hippopotas|-1|-1|Field|
            Hippowdon|-1|-1|Field|Hippopotas
            Skorupi|-1|-1|Bug,Water 3|
            Drapion|-1|-1|Bug,Water 3|Skorupi
            Croagunk|-1|-1|Human-Like|
            Toxicroak|-1|-1|Human-Like|Croagunk
            Carnivine|-1|-1|Grass|
            Finneon|-1|-1|Water 2|
            Lumineon|-1|-1|Water 2|Finneon
            Mantyke|-1|-1|Undiscovered|
            Snover|-1|-1|Monster,Grass|
            Abomasnow|-1|-1|Monster,Grass|Snover
            Weavile|-1|-1|Field|Sneasel
            Magnezone|-1|-1|Mineral|Magneton
            Lickilicky|-1|-1|Monster|Lickitung
            Rhyperior|-1|-1|Monster,Field|Rhydon
            Tangrowth|-1|-1|Grass|Tangela
            Electivire|0.75|0.25|Human-Like|Electabuzz
            Magmortar|0.75|0.25|Human-Like|Magmar
            Togekiss|0.875|0.125|Flying,Fairy|Togetic
            Yanmega|-1|-1|Bug|Yanma
            Leafeon|0.875|0.125|Field|Eevee
            Glaceon|0.875|0.125|Field|Eevee
            Gliscor|-1|-1|Bug|Gligar
            Mamoswine|-1|-1|Field|Piloswine
            Porygon-Z|-1|-1|Mineral|Porygon2
            Gallade|-1|-1|Human-Like,Amorphous|Kirlia
            Probopass|-1|-1|Mineral|Nosepass
            Dusknoir|-1|-1|Amorphous|Dusclops
            Froslass|-1|-1|Fairy,Mineral|Snorunt
            Rotom|-1|-1|Amorphous|
            Uxie|-1|-1|Undiscovered|
            Mesprit|-1|-1|Undiscovered|
            Azelf|-1|-1|Undiscovered|
            Dialga|-1|-1|Undiscovered|
            Palkia|-1|-1|Undiscovered|
            Heatran|-1|-1|Undiscovered|
            Regigigas|-1|-1|Undiscovered|
            Giratina|-1|-1|Undiscovered|
            Cresselia|-1|-1|Undiscovered|
            Phione|-1|-1|Water 1,Fairy|
            Manaphy|-1|-1|Water 1,Fairy|
            Darkrai|-1|-1|Undiscovered|
            Shaymin|-1|-1|Undiscovered|
            Arceus|-1|-1|Undiscovered|
            Victini|-1|-1|Undiscovered|
            Snivy|0.875|0.125|Field,Grass|
            Servine|0.875|0.125|Field,Grass|Snivy
            Serperior|0.875|0.125|Field,Grass|Servine
            Tepig|0.875|0.125|Field|
            Pignite|0.875|0.125|Field|Tepig
            Emboar|0.875|0.125|Field|Pignite
            Oshawott|0.875|0.125|Field|
            Dewott|0.875|0.125|Field|Oshawott
            Samurott|0.875|0.125|Field|Dewott
            Patrat|-1|-1|Field|
            Watchog|-1|-1|Field|Patrat
            Lillipup|-1|-1|Field|
            Herdier|-1|-1|Field|Lillipup
            Stoutland|-1|-1|Field|Herdier
            Purrloin|-1|-1|Field|
            Liepard|-1|-1|Field|Purrloin
            Pansage|0.875|0.125|Field|
            Simisage|0.875|0.125|Field|Pansage
            Pansear|0.875|0.125|Field|
            Simisear|0.875|0.125|Field|Pansear
            Panpour|0.875|0.125|Field|
            Simipour|0.875|0.125|Field|Panpour
            Munna|-1|-1|Field|
            Musharna|-1|-1|Field|Munna
            Pidove|-1|-1|Flying|
            Tranquill|-1|-1|Flying|Pidove
            Unfezant|-1|-1|Flying|Tranquill
            Blitzle|-1|-1|Field|
            Zebstrika|-1|-1|Field|Blitzle
            Roggenrola|-1|-1|Mineral|
            Boldore|-1|-1|Mineral|Roggenrola
            Gigalith|-1|-1|Mineral|Boldore
            Woobat|-1|-1|Flying,Field|
            Swoobat|-1|-1|Flying,Field|Woobat
            Drilbur|-1|-1|Field|
            Excadrill|-1|-1|Field|Drilbur
            Audino|-1|-1|Fairy|
            Timburr|0.75|0.25|Human-Like|
            Gurdurr|0.75|0.25|Human-Like|Timburr
            Conkeldurr|0.75|0.25|Human-Like|Gurdurr
            Tympole|-1|-1|Water 1|
            Palpitoad|-1|-1|Water 1|Tympole
            Seismitoad|-1|-1|Water 1|Palpitoad
            Throh|-1|-1|Human-Like|
            Sawk|-1|-1|Human-Like|
            Sewaddle|-1|-1|Bug|
            Swadloon|-1|-1|Bug|Sewaddle
            Leavanny|-1|-1|Bug|Swadloon
            Venipede|-1|-1|Bug|
            Whirlipede|-1|-1|Bug|Venipede
            Scolipede|-1|-1|Bug|Whirlipede
            Cottonee|-1|-1|Fairy,Grass|
            Whimsicott|-1|-1|Fairy,Grass|Cottonee
            Petilil|-1|-1|Grass|
            Lilligant|-1|-1|Grass|Petilil
            Basculin|-1|-1|Water 2|
            Sandile|-1|-1|Field|
            Krokorok|-1|-1|Field|Sandile
            Krookodile|-1|-1|Field|Krokorok
            Darumaka|-1|-1|Field|
            Darmanitan|-1|-1|Field|Darumaka
            Maractus|-1|-1|Grass|
            Dwebble|-1|-1|Bug,Mineral|
            Crustle|-1|-1|Bug,Mineral|Dwebble
            Scraggy|-1|-1|Field,Dragon|
            Scrafty|-1|-1|Field,Dragon|Scraggy
            Sigilyph|-1|-1|Flying|
            Yamask|-1|-1|Mineral,Amorphous|
            Cofagrigus|-1|-1|Mineral,Amorphous|Yamask
            Tirtouga|0.875|0.125|Water 1,Water 3|
            Carracosta|0.875|0.125|Water 1,Water 3|Tirtouga
            Archen|0.875|0.125|Flying,Water 3|
            Archeops|0.875|0.125|Flying,Water 3|Archen
            Trubbish|-1|-1|Mineral|
            Garbodor|-1|-1|Mineral|Trubbish
            Zorua|0.875|0.125|Field|
            Zoroark|0.875|0.125|Field|Zorua
            Minccino|0.25|0.75|Field|
            Cinccino|0.25|0.75|Field|Minccino
            Gothita|0.25|0.75|Human-Like|
            Gothorita|0.25|0.75|Human-Like|Gothita
            Gothitelle|0.25|0.75|Human-Like|Gothorita
            Solosis|-1|-1|Amorphous|
            Duosion|-1|-1|Amorphous|Solosis
            Reuniclus|-1|-1|Amorphous|Duosion
            Ducklett|-1|-1|Water 1,Flying|
            Swanna|-1|-1|Water 1,Flying|Ducklett
            Vanillite|-1|-1|Mineral|
            Vanillish|-1|-1|Mineral|Vanillite
            Vanilluxe|-1|-1|Mineral|Vanillish
            Deerling|-1|-1|Field|
            Sawsbuck|-1|-1|Field|Deerling
            Emolga|-1|-1|Field|
            Karrablast|-1|-1|Bug|
            Escavalier|-1|-1|Bug|Karrablast
            Foongus|-1|-1|Grass|
            Amoonguss|-1|-1|Grass|Foongus
            Frillish|-1|-1|Amorphous|
            Jellicent|-1|-1|Amorphous|Frillish
            Alomomola|-1|-1|Water 1,Water 2|
            Joltik|-1|-1|Bug|
            Galvantula|-1|-1|Bug|Joltik
            Ferroseed|-1|-1|Grass,Mineral|
            Ferrothorn|-1|-1|Grass,Mineral|Ferroseed
            Klink|-1|-1|Mineral|
            Klang|-1|-1|Mineral|Klink
            Klinklang|-1|-1|Mineral|Klang
            Tynamo|-1|-1|Amorphous|
            Eelektrik|-1|-1|Amorphous|Tynamo
            Eelektross|-1|-1|Amorphous|Eelektrik
            Elgyem|-1|-1|Human-Like|
            Beheeyem|-1|-1|Human-Like|Elgyem
            Litwick|-1|-1|Amorphous|
            Lampent|-1|-1|Amorphous|Litwick
            Chandelure|-1|-1|Amorphous|Lampent
            Axew|-1|-1|Monster,Dragon|
            Fraxure|-1|-1|Monster,Dragon|Axew
            Haxorus|-1|-1|Monster,Dragon|Fraxure
            Cubchoo|-1|-1|Field|
            Beartic|-1|-1|Field|Cubchoo
            Cryogonal|-1|-1|Mineral|
            Shelmet|-1|-1|Bug|
            Accelgor|-1|-1|Bug|Shelmet
            Stunfisk|-1|-1|Water 1,Amorphous|
            Mienfoo|-1|-1|Field,Human-Like|
            Mienshao|-1|-1|Field,Human-Like|Mienfoo
            Druddigon|-1|-1|Monster,Dragon|
            Golett|-1|-1|Mineral|
            Golurk|-1|-1|Mineral|Golett
            Pawniard|-1|-1|Human-Like|
            Bisharp|-1|-1|Human-Like|Pawniard
            Bouffalant|-1|-1|Field|
            Rufflet|-1|-1|Flying|
            Braviary|-1|-1|Flying|Rufflet
            Vullaby|-1|-1|Flying|
            Mandibuzz|-1|-1|Flying|Vullaby
            Heatmor|-1|-1|Field|
            Durant|-1|-1|Bug|
            Deino|-1|-1|Dragon|
            Zweilous|-1|-1|Dragon|Deino
            Hydreigon|-1|-1|Dragon|Zweilous
            Larvesta|-1|-1|Bug|
            Volcarona|-1|-1|Bug|Larvesta
            Cobalion|-1|-1|Undiscovered|
            Terrakion|-1|-1|Undiscovered|
            Virizion|-1|-1|Undiscovered|
            Tornadus|-1|-1|Undiscovered|
            Thundurus|-1|-1|Undiscovered|
            Reshiram|-1|-1|Undiscovered|
            Zekrom|-1|-1|Undiscovered|
            Landorus|-1|-1|Undiscovered|
            Kyurem|-1|-1|Undiscovered|
            Keldeo|-1|-1|Undiscovered|
            Meloetta|-1|-1|Undiscovered|
            Genesect|-1|-1|Undiscovered|
            Chespin|0.875|0.125|Field|
            Quilladin|0.875|0.125|Field|Chespin
            Chesnaught|0.875|0.125|Field|Quilladin
            Fennekin|0.875|0.125|Field|
            Braixen|0.875|0.125|Field|Fennekin
            Delphox|0.875|0.125|Field|Braixen
            Froakie|0.875|0.125|Water 1|
            Frogadier|0.875|0.125|Water 1|Froakie
            Greninja|0.875|0.125|Water 1|Frogadier
            Bunnelby|-1|-1|Field|
            Diggersby|-1|-1|Field|Bunnelby
            Fletchling|-1|-1|Flying|
            Fletchinder|-1|-1|Flying|Fletchling
            Talonflame|-1|-1|Flying|Fletchinder
            Scatterbug|-1|-1|Bug|
            Spewpa|-1|-1|Bug|Scatterbug
            Vivillon|-1|-1|Bug|Spewpa
            Litleo|0.125|0.875|Field|
            Pyroar|0.125|0.875|Field|Litleo
            Flabébé|-1|-1|Fairy|
            Floette|-1|-1|Fairy|Flabe\u0301be\u0301
            Florges|-1|-1|Fairy|Floette
            Skiddo|-1|-1|Field|
            Gogoat|-1|-1|Field|Skiddo
            Pancham|-1|-1|Field,Human-Like|
            Pangoro|-1|-1|Field,Human-Like|Pancham
            Furfrou|-1|-1|Field|
            Espurr|-1|-1|Field|
            Meowstic|-1|-1|Field|Espurr
            Honedge|-1|-1|Mineral|
            Doublade|-1|-1|Mineral|Honedge
            Aegislash|-1|-1|Mineral|Doublade
            Spritzee|-1|-1|Fairy|
            Aromatisse|-1|-1|Fairy|Spritzee
            Swirlix|-1|-1|Fairy|
            Slurpuff|-1|-1|Fairy|Swirlix
            Inkay|-1|-1|Water 1,Water 2|
            Malamar|-1|-1|Water 1,Water 2|Inkay
            Binacle|-1|-1|Water 3|
            Barbaracle|-1|-1|Water 3|Binacle
            Skrelp|-1|-1|Water 1,Dragon|
            Dragalge|-1|-1|Water 1,Dragon|Skrelp
            Clauncher|-1|-1|Water 1,Water 3|
            Clawitzer|-1|-1|Water 1,Water 3|Clauncher
            Helioptile|-1|-1|Monster,Dragon|
            Heliolisk|-1|-1|Monster,Dragon|Helioptile
            Tyrunt|0.875|0.125|Monster,Dragon|
            Tyrantrum|0.875|0.125|Monster,Dragon|Tyrunt
            Amaura|0.875|0.125|Monster|
            Aurorus|0.875|0.125|Monster|Amaura
            Sylveon|0.875|0.125|Field|Eevee
            Hawlucha|-1|-1|Flying,Human-Like|
            Dedenne|-1|-1|Field,Fairy|
            Carbink|-1|-1|Fairy,Mineral|
            Goomy|-1|-1|Dragon|
            Sliggoo|-1|-1|Dragon|Goomy
            Goodra|-1|-1|Dragon|Sliggoo
            Klefki|-1|-1|Mineral|
            Phantump|-1|-1|Grass,Amorphous|
            Trevenant|-1|-1|Grass,Amorphous|Phantump
            Pumpkaboo|-1|-1|Amorphous|
            Gourgeist|-1|-1|Amorphous|Pumpkaboo
            Bergmite|-1|-1|Monster,Mineral|
            Avalugg|-1|-1|Monster,Mineral|Bergmite
            Noibat|-1|-1|Flying,Dragon|
            Noivern|-1|-1|Flying,Dragon|Noibat
            Xerneas|-1|-1|Undiscovered|
            Yveltal|-1|-1|Undiscovered|
            Zygarde|-1|-1|Undiscovered|
            Diancie|-1|-1|Undiscovered|
            Hoopa|-1|-1|Undiscovered|
            Volcanion|-1|-1|Undiscovered|
            Rowlet|0.875|0.125|Flying|
            Dartrix|0.875|0.125|Flying|Rowlet
            Decidueye|0.875|0.125|Flying|Dartrix
            Litten|0.875|0.125|Field|
            Torracat|0.875|0.125|Field|Litten
            Incineroar|0.875|0.125|Field|Torracat
            Popplio|0.875|0.125|Water 1,Field|
            Brionne|0.875|0.125|Water 1,Field|Popplio
            Primarina|0.875|0.125|Water 1,Field|Brionne
            Pikipek|-1|-1|Flying|
            Trumbeak|-1|-1|Flying|Pikipek
            Toucannon|-1|-1|Flying|Trumbeak
            Yungoos|-1|-1|Field|
            Gumshoos|-1|-1|Field|Yungoos
            Grubbin|-1|-1|Bug|
            Charjabug|-1|-1|Bug|Grubbin
            Vikavolt|-1|-1|Bug|Charjabug
            Crabrawler|-1|-1|Water 3|
            Crabominable|-1|-1|Water 3|Crabrawler
            Oricorio|0.25|0.75|Flying|
            Cutiefly|-1|-1|Bug,Fairy|
            Ribombee|-1|-1|Bug,Fairy|Cutiefly
            Rockruff|-1|-1|Field|
            Lycanroc|-1|-1|Field|Rockruff
            Wishiwashi|-1|-1|Water 2|
            Mareanie|-1|-1|Water 1|
            Toxapex|-1|-1|Water 1|Mareanie
            Mudbray|-1|-1|Field|
            Mudsdale|-1|-1|Field|Mudbray
            Dewpider|-1|-1|Water 1,Bug|
            Araquanid|-1|-1|Water 1,Bug|Dewpider
            Fomantis|-1|-1|Grass|
            Lurantis|-1|-1|Grass|Fomantis
            Morelull|-1|-1|Grass|
            Shiinotic|-1|-1|Grass|Morelull
            Salandit|0.875|0.125|Monster,Dragon|
            Salazzle|-1|-1|Monster,Dragon|Salandit
            Stufful|-1|-1|Field|
            Bewear|-1|-1|Field|Stufful
            Bounsweet|-1|-1|Grass|
            Steenee|-1|-1|Grass|Bounsweet
            Tsareena|-1|-1|Grass|Steenee
            Comfey|0.25|0.75|Grass|
            Oranguru|-1|-1|Field|
            Passimian|-1|-1|Field|
            Wimpod|-1|-1|Bug,Water 3|
            Golisopod|-1|-1|Bug,Water 3|Wimpod
            Sandygast|-1|-1|Amorphous|
            Palossand|-1|-1|Amorphous|Sandygast
            Pyukumuku|-1|-1|Water 1|
            Type: Null|-1|-1|Undiscovered|
            Silvally|-1|-1|Undiscovered|Type: Null
            Minior|-1|-1|Mineral|
            Komala|-1|-1|Field|
            Turtonator|-1|-1|Monster,Dragon|
            Togedemaru|-1|-1|Field,Fairy|
            Mimikyu|-1|-1|Amorphous|
            Bruxish|-1|-1|Water 2|
            Drampa|-1|-1|Monster,Dragon|
            Dhelmise|-1|-1|Mineral|
            Jangmo-o|-1|-1|Dragon|
            Hakamo-o|-1|-1|Dragon|Jangmo-o
            Kommo-o|-1|-1|Dragon|Hakamo-o
            Tapu Koko|-1|-1|Undiscovered|
            Tapu Lele|-1|-1|Undiscovered|
            Tapu Bulu|-1|-1|Undiscovered|
            Tapu Fini|-1|-1|Undiscovered|
            Cosmog|-1|-1|Undiscovered|
            Cosmoem|-1|-1|Undiscovered|Cosmog
            Solgaleo|-1|-1|Undiscovered|Cosmoem
            Lunala|-1|-1|Undiscovered|Cosmoem
            Nihilego|-1|-1|Undiscovered|
            Buzzwole|-1|-1|Undiscovered|
            Pheromosa|-1|-1|Undiscovered|
            Xurkitree|-1|-1|Undiscovered|
            Celesteela|-1|-1|Undiscovered|
            Kartana|-1|-1|Undiscovered|
            Guzzlord|-1|-1|Undiscovered|
            Necrozma|-1|-1|Undiscovered|
            Magearna|-1|-1|Undiscovered|
            Marshadow|-1|-1|Undiscovered|
            Poipole|-1|-1|Undiscovered|
            Naganadel|-1|-1|Undiscovered|Poipole
            Stakataka|-1|-1|Undiscovered|
            Blacephalon|-1|-1|Undiscovered|
            Zeraora|-1|-1|Undiscovered|
            Meltan|-1|-1|Undiscovered|
            Melmetal|-1|-1|Undiscovered|
            Grookey|0.875|0.125|Field,Grass|
            Thwackey|0.875|0.125|Field,Grass|Grookey
            Rillaboom|0.875|0.125|Field,Grass|Thwackey
            Scorbunny|0.875|0.125|Field,Human-Like|
            Raboot|0.875|0.125|Field,Human-Like|Scorbunny
            Cinderace|0.875|0.125|Field,Human-Like|Raboot
            Sobble|0.875|0.125|Water 1,Field|
            Drizzile|0.875|0.125|Water 1,Field|Sobble
            Inteleon|0.875|0.125|Water 1,Field|Drizzile
            Skwovet|-1|-1|Field|
            Greedent|-1|-1|Field|Skwovet
            Rookidee|-1|-1|Flying|
            Corvisquire|-1|-1|Flying|Rookidee
            Corviknight|-1|-1|Flying|Corvisquire
            Blipbug|-1|-1|Bug|
            Dottler|-1|-1|Bug|Blipbug
            Orbeetle|-1|-1|Bug|Dottler
            Nickit|-1|-1|Field|
            Thievul|-1|-1|Field|Nickit
            Gossifleur|-1|-1|Grass|
            Eldegoss|-1|-1|Grass|Gossifleur
            Wooloo|-1|-1|Field|
            Dubwool|-1|-1|Field|Wooloo
            Chewtle|-1|-1|Monster,Water 1|
            Drednaw|-1|-1|Monster,Water 1|Chewtle
            Yamper|-1|-1|Field|
            Boltund|-1|-1|Field|Yamper
            Rolycoly|-1|-1|Mineral|
            Carkol|-1|-1|Mineral|Rolycoly
            Coalossal|-1|-1|Mineral|Carkol
            Applin|-1|-1|Grass,Dragon|
            Flapple|-1|-1|Grass,Dragon|Applin
            Appletun|-1|-1|Grass,Dragon|Applin
            Silicobra|-1|-1|Field,Dragon|
            Sandaconda|-1|-1|Field,Dragon|Silicobra
            Cramorant|-1|-1|Water 1,Flying|
            Arrokuda|-1|-1|Water 2|
            Barraskewda|-1|-1|Water 2|Arrokuda
            Toxel|-1|-1|Undiscovered|
            Toxtricity|-1|-1|Human-Like|Toxel
            Sizzlipede|-1|-1|Bug|
            Centiskorch|-1|-1|Bug|Sizzlipede
            Clobbopus|-1|-1|Water 1,Human-Like|
            Grapploct|-1|-1|Water 1,Human-Like|Clobbopus
            Sinistea|-1|-1|Mineral,Amorphous|
            Polteageist|-1|-1|Mineral,Amorphous|Sinistea
            Hatenna|-1|-1|Fairy|
            Hattrem|-1|-1|Fairy|Hatenna
            Hatterene|-1|-1|Fairy|Hattrem
            Impidimp|-1|-1|Fairy,Human-Like|
            Morgrem|-1|-1|Fairy,Human-Like|Impidimp
            Grimmsnarl|-1|-1|Fairy,Human-Like|Morgrem
            Obstagoon|-1|-1|Field|Linoone-Galar
            Perrserker|-1|-1|Field|Meowth-Galar
            Cursola|0.25|0.75|Water 1,Water 3|Corsola-Galar
            Sirfetch'd|-1|-1|Flying,Field|Farfetch\u2019d-Galar
            Mr. Rime|-1|-1|Human-Like|Mr. Mime-Galar
            Runerigus|-1|-1|Mineral,Amorphous|Yamask-Galar
            Milcery|-1|-1|Fairy,Amorphous|
            Alcremie|-1|-1|Fairy,Amorphous|Milcery
            Falinks|-1|-1|Fairy,Mineral|
            Pincurchin|-1|-1|Water 1,Amorphous|
            Snom|-1|-1|Bug|
            Frosmoth|-1|-1|Bug|Snom
            Stonjourner|-1|-1|Mineral|
            Eiscue|-1|-1|Water 1,Field|
            Indeedee|-1|-1|Fairy|
            Morpeko|-1|-1|Field,Fairy|
            Cufant|-1|-1|Field,Mineral|
            Copperajah|-1|-1|Field,Mineral|Cufant
            Dracozolt|-1|-1|Undiscovered|
            Arctozolt|-1|-1|Undiscovered|
            Dracovish|-1|-1|Undiscovered|
            Arctovish|-1|-1|Undiscovered|
            Duraludon|-1|-1|Mineral,Dragon|
            Dreepy|-1|-1|Amorphous,Dragon|
            Drakloak|-1|-1|Amorphous,Dragon|Dreepy
            Dragapult|-1|-1|Amorphous,Dragon|Drakloak
            Zacian|-1|-1|Undiscovered|
            Zamazenta|-1|-1|Undiscovered|
            Eternatus|-1|-1|Undiscovered|
            Kubfu|0.875|0.125|Undiscovered|
            Urshifu|0.875|0.125|Undiscovered|Kubfu
            Zarude|-1|-1|Undiscovered|
            Regieleki|-1|-1|Undiscovered|
            Regidrago|-1|-1|Undiscovered|
            Glastrier|-1|-1|Undiscovered|
            Spectrier|-1|-1|Undiscovered|
            Calyrex|-1|-1|Undiscovered|
            Wyrdeer|-1|-1|Field|Stantler
            Kleavor|-1|-1|Bug|Scyther
            Ursaluna|-1|-1|Field|Ursaring
            Basculegion|-1|-1|Water 2|Basculin-White-Striped
            Sneasler|-1|-1|Field|Sneasel-Hisui
            Overqwil|-1|-1|Water 2|Qwilfish-Hisui
            Enamorus|-1|-1|Undiscovered|
            Sprigatito|0.875|0.125|Field,Grass|
            Floragato|0.875|0.125|Field,Grass|Sprigatito
            Meowscarada|0.875|0.125|Field,Grass|Floragato
            Fuecoco|0.875|0.125|Field|
            Crocalor|0.875|0.125|Field|Fuecoco
            Skeledirge|0.875|0.125|Field|Crocalor
            Quaxly|0.875|0.125|Flying,Water 1|
            Quaxwell|0.875|0.125|Flying,Water 1|Quaxly
            Quaquaval|0.875|0.125|Flying,Water 1|Quaxwell
            Lechonk|-1|-1|Field|
            Oinkologne|-1|-1|Field|Lechonk
            Tarountula|-1|-1|Bug|
            Spidops|-1|-1|Bug|Tarountula
            Nymble|-1|-1|Bug|
            Lokix|-1|-1|Bug|Nymble
            Pawmi|-1|-1|Field|
            Pawmo|-1|-1|Field|Pawmi
            Pawmot|-1|-1|Field|Pawmo
            Tandemaus|-1|-1|Field,Fairy|
            Maushold|-1|-1|Field,Fairy|Tandemaus
            Fidough|-1|-1|Field,Mineral|
            Dachsbun|-1|-1|Field,Mineral|Fidough
            Smoliv|-1|-1|Grass|
            Dolliv|-1|-1|Grass|Smoliv
            Arboliva|-1|-1|Grass|Dolliv
            Squawkabilly|-1|-1|Flying|
            Nacli|-1|-1|Mineral|
            Naclstack|-1|-1|Mineral|Nacli
            Garganacl|-1|-1|Mineral|Naclstack
            Charcadet|-1|-1|Human-Like|
            Armarouge|-1|-1|Human-Like|Charcadet
            Ceruledge|-1|-1|Human-Like|Charcadet
            Tadbulb|-1|-1|Water 1|
            Bellibolt|-1|-1|Water 1|Tadbulb
            Wattrel|-1|-1|Water 1,Flying|
            Kilowattrel|-1|-1|Water 1,Flying|Wattrel
            Maschiff|-1|-1|Field|
            Mabosstiff|-1|-1|Field|Maschiff
            Shroodle|-1|-1|Field|
            Grafaiai|-1|-1|Field|Shroodle
            Bramblin|-1|-1|Grass|
            Brambleghast|-1|-1|Grass|Bramblin
            Toedscool|-1|-1|Field|
            Toedscruel|-1|-1|Field|Toedscool
            Klawf|-1|-1|Water 3|
            Capsakid|-1|-1|Grass|
            Scovillain|-1|-1|Grass|Capsakid
            Rellor|-1|-1|Bug|
            Rabsca|-1|-1|Bug|Rellor
            Flittle|-1|-1|Flying|
            Espathra|-1|-1|Flying|Flittle
            Tinkatink|-1|-1|Fairy|
            Tinkatuff|-1|-1|Fairy|Tinkatink
            Tinkaton|-1|-1|Fairy|Tinkatuff
            Wiglett|-1|-1|Water 3|
            Wugtrio|-1|-1|Water 3|Wiglett
            Bombirdier|-1|-1|Flying|
            Finizen|-1|-1|Field,Water 2|
            Palafin|-1|-1|Field,Water 2|Finizen
            Varoom|-1|-1|Mineral|
            Revavroom|-1|-1|Mineral|Varoom
            Cyclizar|-1|-1|Field|
            Orthworm|-1|-1|Field|
            Glimmet|-1|-1|Mineral|
            Glimmora|-1|-1|Mineral|Glimmet
            Greavard|-1|-1|Field|
            Houndstone|-1|-1|Field|Greavard
            Flamigo|-1|-1|Flying|
            Cetoddle|-1|-1|Field|
            Cetitan|-1|-1|Field|Cetoddle
            Veluza|-1|-1|Water 2|
            Dondozo|-1|-1|Water 2|
            Tatsugiri|-1|-1|Water 2|
            Annihilape|-1|-1|Field|Primeape
            Clodsire|-1|-1|Water 1,Field|Wooper-Paldea
            Farigiraf|-1|-1|Field|Girafarig
            Dudunsparce|-1|-1|Field|Dunsparce
            Kingambit|-1|-1|Human-Like|Bisharp
            Great Tusk|-1|-1|Undiscovered|
            Scream Tail|-1|-1|Undiscovered|
            Brute Bonnet|-1|-1|Undiscovered|
            Flutter Mane|-1|-1|Undiscovered|
            Slither Wing|-1|-1|Undiscovered|
            Sandy Shocks|-1|-1|Undiscovered|
            Iron Treads|-1|-1|Undiscovered|
            Iron Bundle|-1|-1|Undiscovered|
            Iron Hands|-1|-1|Undiscovered|
            Iron Jugulis|-1|-1|Undiscovered|
            Iron Moth|-1|-1|Undiscovered|
            Iron Thorns|-1|-1|Undiscovered|
            Frigibax|-1|-1|Dragon,Mineral|
            Arctibax|-1|-1|Dragon,Mineral|Frigibax
            Baxcalibur|-1|-1|Dragon,Mineral|Arctibax
            Gimmighoul|-1|-1|Undiscovered|
            Gholdengo|-1|-1|Undiscovered|Gimmighoul
            Wo-Chien|-1|-1|Undiscovered|
            Chien-Pao|-1|-1|Undiscovered|
            Ting-Lu|-1|-1|Undiscovered|
            Chi-Yu|-1|-1|Undiscovered|
            Roaring Moon|-1|-1|Undiscovered|
            Iron Valiant|-1|-1|Undiscovered|
            Koraidon|-1|-1|Undiscovered|
            Miraidon|-1|-1|Undiscovered|
            Walking Wake|-1|-1|Undiscovered|
            Iron Leaves|-1|-1|Undiscovered|
            Dipplin|-1|-1|Grass,Dragon|Applin
            Poltchageist|-1|-1|Mineral,Amorphous|
            Sinistcha|-1|-1|Mineral,Amorphous|Poltchageist
            Okidogi|-1|-1|Undiscovered|
            Munkidori|-1|-1|Undiscovered|
            Fezandipiti|-1|-1|Undiscovered|
            Ogerpon|-1|-1|Undiscovered|
            Archaludon|-1|-1|Mineral,Dragon|Duraludon
            Hydrapple|-1|-1|Grass,Dragon|Dipplin
            Gouging Fire|-1|-1|Undiscovered|
            Raging Bolt|-1|-1|Undiscovered|
            Iron Boulder|-1|-1|Undiscovered|
            Iron Crown|-1|-1|Undiscovered|
            Terapagos|-1|-1|Undiscovered|
            Pecharunt|-1|-1|Undiscovered|
            """;

    public static final class Datos {
        private final String nombre;
        private final double probMacho, probHembra;
        private final Set<String> gruposHuevo;
        private final String preEvolucion;

        private Datos(String nombre, double probMacho, double probHembra, Set<String> gruposHuevo, String preEvolucion) {
            this.nombre = nombre;
            this.probMacho = probMacho;
            this.probHembra = probHembra;
            this.gruposHuevo = Collections.unmodifiableSet(gruposHuevo);
            this.preEvolucion = preEvolucion;
        }
        public String nombre() { return nombre; }
        public double probabilidadMacho() { return probMacho; }
        public double probabilidadHembra() { return probHembra; }
        public Set<String> gruposHuevo() { return gruposHuevo; }
        public String preEvolucion() { return preEvolucion; }
        public boolean tieneSexo() { return probMacho >= 0 && probHembra >= 0; }
        public boolean puedeCriar() {
            return !gruposHuevo.isEmpty() && !gruposHuevo.contains("Undiscovered");
        }
    }

    private static final Map<String, Datos> POR_NOMBRE = cargar();

    private static Map<String, Datos> cargar() {
        Map<String, Datos> mapa = new HashMap<>();
        for (String linea : DATOS.split("\\R")) {
            if (linea.isBlank()) continue;
            String[] p = linea.split("\\|", -1);
            if (p.length < 5) continue;
            Set<String> grupos = new LinkedHashSet<>();
            for (String grupo : p[3].split(",")) if (!grupo.isBlank()) grupos.add(grupo.trim());
            double macho, hembra;
            try {
                macho = Double.parseDouble(p[1]);
                hembra = Double.parseDouble(p[2]);
            } catch (NumberFormatException ex) {
                macho = -1;
                hembra = -1;
            }
            Datos datos = new Datos(p[0], macho, hembra, grupos, p[4].isBlank() ? null : p[4]);
            mapa.put(normalizar(p[0]), datos);
        }
        // Ditto es especial en este juego: puede actuar como progenitor macho o hembra.
        mapa.put(normalizar("Ditto"), new Datos("Ditto", -1, -1, Set.of("Ditto"), null));
        return Collections.unmodifiableMap(mapa);
    }

    public static String normalizar(String nombre) {
        if (nombre == null) return "";
        String limpio = nombre.replace("♀", "f").replace("♂", "m");
        limpio = Normalizer.normalize(limpio, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return limpio.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    public static Datos de(PokemonBase base) {
        if (base == null) return null;
        Datos d = POR_NOMBRE.get(normalizar(base.getNombre()));
        if (d != null) return d;
        // No se permite criar formas o criaturas que no figuren en el catálogo genético.
        return new Datos(base.getNombre(), -1, -1, Set.of("Undiscovered"), null);
    }

    public static SexoPokemon sexoAleatorio(PokemonBase base) {
        if (base != null && base.getNombre().equalsIgnoreCase("Ditto")) return SexoPokemon.AMBOS;
        Datos d = de(base);
        if (d == null || !d.tieneSexo()) return SexoPokemon.SIN_GENERO;
        if (d.probMacho <= 0) return SexoPokemon.HEMBRA;
        if (d.probHembra <= 0) return SexoPokemon.MACHO;
        return ThreadLocalRandom.current().nextDouble() < d.probMacho
                ? SexoPokemon.MACHO : SexoPokemon.HEMBRA;
    }

    public static boolean puedeCriar(PokemonBase base) {
        if (base == null) return false;
        Datos d = de(base);
        return d != null && d.puedeCriar();
    }

    public static boolean compartenGrupoHuevo(PokemonBase a, PokemonBase b) {
        Datos da = de(a), db = de(b);
        if (da == null || db == null || !da.puedeCriar() || !db.puedeCriar()) return false;
        for (String grupo : da.gruposHuevo) if (db.gruposHuevo.contains(grupo)) return true;
        return false;
    }

    /** Devuelve la especie de huevo (raíz de la línea evolutiva) del progenitor. */
    public static PokemonBase especieHuevo(PokemonBase progenitor) {
        if (progenitor == null) return null;
        PokemonBase actual = progenitor;
        Set<String> visitados = new HashSet<>();
        while (true) {
            String clave = normalizar(actual.getNombre());
            if (!visitados.add(clave)) break;
            Datos d = de(actual);
            if (d == null || d.preEvolucion == null || d.preEvolucion.isBlank()) break;
            PokemonBase anterior = Pokedex.buscarPorNombre(d.preEvolucion);
            if (anterior == null) break;
            actual = anterior;
        }
        return actual;
    }

    public static int numeroEspeciesConDatos() { return POR_NOMBRE.size(); }
}
