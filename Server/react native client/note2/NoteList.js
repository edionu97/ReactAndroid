import React, { Component } from 'react';
import { Text, View, ActivityIndicator } from 'react-native';
import styles from '../core/styles';
import { getLogger, issueToText } from '../core/utils';
import { NoteView } from "./NoteView";
import { Consumer } from './context';

const log = getLogger('NoteList');

export class NoteList extends Component {
  constructor(props) {
    super(props);
    log('constructor');
  }

  render() {
    log('render');
    return (
      <Consumer>
        {({isLoading, issue, notes}) => (
          <View style={styles.content}>
            <ActivityIndicator animating={isLoading} style={styles.activityIndicator} size="large"/>
            {issue && <Text>{issueToText(issue)}</Text>}
            {notes && notes.map(note => <NoteView key={note.id} note={note}/>)}
          </View>
          )}
      </Consumer>
    );
  }
}
