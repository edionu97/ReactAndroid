import React from 'react';
import NoteStore from './NoteStore';
import {NoteList} from './NoteList';

export default () => (
  <NoteStore>
    <NoteList/>
  </NoteStore>
);