(ns chbme.en)

(defonce _key "en_")
(defonce title "Self-awareness checklist")
(defonce items-prefix "I notice my ")
(defonce items [
      "physical environment",
      "breath",
      "body",
      "body weight",
      ["body sensations",
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
        ]}]
      "feelings",
      "mental state",
      "thoughts"
])


