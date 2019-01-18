import React, { Component } from 'react';
import { Text, View, ActivityIndicator } from 'react-native';
import styles from '../core/styles';
import { getLogger, issueToText } from '../core/utils';
import { NoteView } from "./NoteView";
import { loadNotes, connectWs, disconnectWs } from "./service";

const log = getLogger('NoteList');

export class NoteList extends Component {
  constructor(props) {
    super(props);
    log('constructor');
    this.state = { };
  }

  render() {
    log('render');
    const { isLoading, issue, notes } = this.state;
    const issueMessage = issueToText(issue);
    return (
      <View style={styles.content}>
        <ActivityIndicator animating={isLoading} style={styles.activityIndicator} size="large"/>
        {issueMessage && <Text>{issueMessage}</Text>}
        {notes && notes.map(note => <NoteView key={note.id} note={note}/>)}
      </View>
    );
  }

  componentDidMount() {
    log('componentDidMount');
    const { store } = this.props;
    store.dispatch(loadNotes());
    this.unsubscribe = store.subscribe(() => {
      const { isLoading, notes, issue } = store.getState().note;
      this.setState({ isLoading, notes, issue });
    });
    this.ws = connectWs(store);
  }

  componentWillUnmount() {
    log('componentWillUnmount');
    this.unsubscribe();
    disconnectWs(this.ws);
  }
}
