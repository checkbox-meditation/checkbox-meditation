(ns chbme.en)

(defonce _key "en_")
(defonce title "Self-awareness checklist")
(defonce items-prefix "I notice my ")
(defonce items [
      "physical environment",
      "breath",
      ["body",
       { 
        :key "bp_"
        :items-prefix ""
        :items [
                "toes and heels",
                "feet",
                "legs",
                "bottom",
                "back",
                "stomach",
                "chest",
                "hands",
                "arms",
                "neck",
                "head",
                "face",
                "top of the head"
        ]}],      
      "body weight",
      ["body sensations",
       {
        :items ["heat",
                "cold",
                "pressure",
                "tickling",
                "expansion",
                "stiffness",
                "tension",
                "trembling",
                "movement",
                "weakness",
                "contact between the skin and the clothes",
                "contact between the feet and the floor",
                "contact between the bottocks and a chair",
                ]}],
      "feelings",
      "mental state",
      "thoughts"
])


