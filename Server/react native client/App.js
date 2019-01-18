import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View
} from 'react-native';
import {createStore, applyMiddleware, combineReducers} from 'redux';
import thunk from 'redux-thunk';
import { NoteList } from './note/NoteList';
import Notes from "./note2";
import { noteReducer } from './note/service';

const rootReducer = combineReducers({ note: noteReducer });
const store = createStore(rootReducer, applyMiddleware(thunk));

export default class App extends Component {
  render() {
    {/*<NoteList store={store}/>*/}
    return (
      <Notes />
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
  },
});
