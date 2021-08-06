import React, { useState } from "react";
import { StyleSheet, FlatList, View } from "react-native";
import Toast from "react-native-toast-message";

import SearchResultsCardComponent from "../components/SearchResultsCardComponent";
import Screen from "../components/Screen";
import gender from "../config/gender";
import requestsApi from "../api/requests";
import AppText from "../components/AppText";
import strings from "../config/strings";
import colors from "../config/colors";

function ScribeSearchResultsScreen({ route, navigation }) {
  const [scribeList, setScribeList] = useState(route.params.data);

  const handleOnPress = async (scribe) => {
    const requestUpdate = {
      scribeId: scribe.id,
      ...route.params.requestUpdate,
    };
    const result = await requestsApi.createRequest(requestUpdate);
    if (result.ok) {
      scribeList.splice(scribeList.indexOf(scribe), 1);
      setScribeList(scribeList.slice());
      Toast.show({
        type: "success",
        text1: strings.request.sent + scribe.firstName,
      });
    } else {
      Toast.show({
        type: "error",
        text1: strings.common.serverError,
      });
    }
  };

  return (
    <Screen>
      {scribeList && scribeList.length == 0 && (
        <View style={styles.emptyContainer}>
          <AppText style={styles.noData}>{strings.request.noData}</AppText>
        </View>
      )}
      {scribeList && scribeList.length > 0 && (
        <FlatList
          data={scribeList}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <SearchResultsCardComponent
              title={item.firstName + " " + item.lastName}
              subTitle={item.email}
              thumbnailIcon={item.gender === gender.MALE ? "male" : "female"}
              onPress={() => handleOnPress(item)}
            ></SearchResultsCardComponent>
          )}
        />
      )}
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
  emptyContainer: { flex: 1, alignItems: "center", justifyContent: "center" },
  noData: { color: colors.medium },
});

export default ScribeSearchResultsScreen;
