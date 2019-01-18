import React, { Component } from 'react';
import { Text, View, StyleSheet } from 'react-native';

export class NoteView extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <View>
        <Text style={styles.listItem}>{this.props.note.text}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  listItem: {
    margin: 10,
  }
});