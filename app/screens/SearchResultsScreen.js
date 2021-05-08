import React from "react";
import { View, StyleSheet, FlatList } from "react-native";
import CardComponent from "../components/CardComponent";
import Screen from "../components/Screen";

function SearchResultsScreen(props) {
  const listing = [
    { id: 1, name: "candidate-1", qualification: "MCA", gender: "M" },
    { id: 2, name: "candidate-2", qualification: "BSc", gender: "F" },
    { id: 3, name: "candidate-3", qualification: "Phd", gender: "M" },
    { id: 4, name: "candidate-4", qualification: "MCA", gender: "F" },
    { id: 5, name: "candidate-5", qualification: "PUC", gender: "M" },
    { id: 6, name: "candidate-6", qualification: "MA", gender: "F" },
    { id: 7, name: "candidate-7", qualification: "MCA", gender: "M" },
    { id: 8, name: "candidate-8", qualification: "MSc", gender: "F" },
  ];
  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <CardComponent
            title={item.name}
            subTitle={item.qualification}
            thumbnailIcon={item.gender === "M" ? "face" : "face-woman"}
          ></CardComponent>
        )}
      ></FlatList>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default SearchResultsScreen;
