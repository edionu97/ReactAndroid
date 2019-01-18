require('source-map-support/register')
module.exports =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(1);


/***/ }),
/* 1 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator__ = __webpack_require__(2);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator__);


var _this = this;

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _asyncToGenerator(fn) { return function () { var gen = fn.apply(this, arguments); return new Promise(function (resolve, reject) { function step(key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { return Promise.resolve(value).then(function (value) { step("next", value); }, function (err) { step("throw", err); }); } } return step("next"); }); }; }

var Koa = __webpack_require__(4);
var app = new Koa();
var server = __webpack_require__(5).createServer(app.callback());
var WebSocket = __webpack_require__(6);
var wss = new WebSocket.Server({ server: server });
var Router = __webpack_require__(7);
var cors = __webpack_require__(8);
var bodyparser = __webpack_require__(9);

app.use(bodyparser()); //1
app.use(cors()); //2
app.use(function () {
  var _ref = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.mark(function _callee(ctx, next) {
    var start, ms;
    return __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            // logger
            start = new Date();
            _context.next = 3;
            return next();

          case 3:
            ms = new Date() - start;

            console.log(ctx.method + ' ' + ctx.url + ' ' + ctx.response.status + ' - ' + ms + 'ms');

          case 5:
          case 'end':
            return _context.stop();
        }
      }
    }, _callee, _this);
  }));

  return function (_x, _x2) {
    return _ref.apply(this, arguments);
  };
}());

app.use(function () {
  var _ref2 = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.mark(function _callee2(ctx, next) {
    return __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            _context2.prev = 0;
            _context2.next = 3;
            return next();

          case 3:
            _context2.next = 9;
            break;

          case 5:
            _context2.prev = 5;
            _context2.t0 = _context2['catch'](0);

            ctx.response.body = { issue: [{ error: _context2.t0.message || 'Unexpected error' }] };
            ctx.response.status = 500; // internal server error

          case 9:
          case 'end':
            return _context2.stop();
        }
      }
    }, _callee2, _this, [[0, 5]]);
  }));

  return function (_x3, _x4) {
    return _ref2.apply(this, arguments);
  };
}());

var Note = function Note(_ref3) {
  var id = _ref3.id,
      text = _ref3.text,
      date = _ref3.date,
      version = _ref3.version;

  _classCallCheck(this, Note);

  this.id = id;
  this.text = text;
  this.date = date;
  this.version = version;
};

var notes = [];
for (var i = 0; i < 21; i++) {
  notes.push(new Note({ id: '' + i, text: 'Note ' + i, date: new Date(Date.now() + i), version: 1 }));
}
var lastUpdated = notes[notes.length - 1].date;
var lastId = notes[notes.length - 1].id;
var pageSize = 10;

var broadcast = function broadcast(data) {
  return wss.clients.forEach(function (client) {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });
};

var router = new Router();
router.get('/note', function (ctx) {
  var ifModifiedSince = ctx.request.get('If-Modified-Since');
  if (ifModifiedSince && new Date(ifModifiedSince).getTime() >= lastUpdated.getTime() - lastUpdated.getMilliseconds()) {
    ctx.response.status = 304; // NOT MODIFIED
    return;
  }
  var text = ctx.request.query.text;
  var page = parseInt(ctx.request.query.page) || 1;
  ctx.response.set('Last-Modified', lastUpdated.toUTCString());
  var sortedNotes = notes.filter(function (note) {
    return text ? note.text.indexOf(text) !== -1 : true;
  }).sort(function (n1, n2) {
    return -(n1.date.getTime() - n2.date.getTime());
  });
  var offset = (page - 1) * pageSize;
  ctx.response.body = {
    page: page,
    notes: sortedNotes.slice(offset, offset + pageSize),
    more: offset + pageSize < sortedNotes.length
  };
  ctx.response.status = 200; // OK
});

router.get('/note/:id', function () {
  var _ref4 = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.mark(function _callee3(ctx) {
    var noteId, note;
    return __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee3$(_context3) {
      while (1) {
        switch (_context3.prev = _context3.next) {
          case 0:
            noteId = ctx.request.params.id;
            note = notes.find(function (note) {
              return noteId === note.id;
            });

            if (note) {
              ctx.response.body = note;
              ctx.response.status = 200; // ok
            } else {
              ctx.response.body = { issue: [{ warning: 'Note with id ' + noteId + ' not found' }] };
              ctx.response.status = 404; // NOT FOUND (if you know the resource was deleted, then return 410 GONE)
            }

          case 3:
          case 'end':
            return _context3.stop();
        }
      }
    }, _callee3, _this);
  }));

  return function (_x5) {
    return _ref4.apply(this, arguments);
  };
}());

var createNote = function () {
  var _ref5 = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.mark(function _callee4(ctx) {
    var note;
    return __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee4$(_context4) {
      while (1) {
        switch (_context4.prev = _context4.next) {
          case 0:
            note = ctx.request.body;

            if (note.text) {
              _context4.next = 5;
              break;
            }

            // validation
            ctx.response.body = { issue: [{ error: 'Text is missing' }] };
            ctx.response.status = 400; //  BAD REQUEST
            return _context4.abrupt('return');

          case 5:
            note.id = '' + (parseInt(lastId) + 1);
            lastId = note.id;
            note.date = Date.now();
            note.version = 1;
            notes.push(note);
            ctx.response.body = note;
            ctx.response.status = 201; // CREATED
            broadcast({ event: 'created', note: note });

          case 13:
          case 'end':
            return _context4.stop();
        }
      }
    }, _callee4, _this);
  }));

  return function createNote(_x6) {
    return _ref5.apply(this, arguments);
  };
}();

router.post('/note', function () {
  var _ref6 = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.mark(function _callee5(ctx) {
    return __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee5$(_context5) {
      while (1) {
        switch (_context5.prev = _context5.next) {
          case 0:
            _context5.next = 2;
            return createNote(ctx);

          case 2:
          case 'end':
            return _context5.stop();
        }
      }
    }, _callee5, _this);
  }));

  return function (_x7) {
    return _ref6.apply(this, arguments);
  };
}());

router.put('/note/:id', function () {
  var _ref7 = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.mark(function _callee6(ctx) {
    var id, note, noteId, index, noteVersion;
    return __WEBPACK_IMPORTED_MODULE_0_C_Users_Eduard_Desktop_PDM_Server_note_server_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee6$(_context6) {
      while (1) {
        switch (_context6.prev = _context6.next) {
          case 0:
            id = ctx.params.id;
            note = ctx.request.body;
            noteId = note.id;

            if (!(noteId && id !== note.id)) {
              _context6.next = 7;
              break;
            }

            ctx.response.body = { issue: [{ error: 'Param id and body id should be the same' }] };
            ctx.response.status = 400; // BAD REQUEST
            return _context6.abrupt('return');

          case 7:
            if (noteId) {
              _context6.next = 11;
              break;
            }

            _context6.next = 10;
            return createNote(ctx);

          case 10:
            return _context6.abrupt('return');

          case 11:
            index = notes.findIndex(function (note) {
              return note.id === id;
            });

            if (!(index === -1)) {
              _context6.next = 16;
              break;
            }

            ctx.response.body = { issue: [{ error: 'Note with id ' + id + ' not found' }] };
            ctx.response.status = 400; // BAD REQUEST
            return _context6.abrupt('return');

          case 16:
            noteVersion = parseInt(ctx.request.get('ETag')) || note.version;

            if (!(noteVersion < notes[index].version)) {
              _context6.next = 21;
              break;
            }

            ctx.response.body = { issue: [{ error: 'Version conflict' }] };
            ctx.response.status = 409; // CONFLICT
            return _context6.abrupt('return');

          case 21:
            note.version++;
            notes[index] = note;
            lastUpdated = new Date();
            ctx.response.body = note;
            ctx.response.status = 200; // OK
            broadcast({ event: 'updated', note: note });

          case 27:
          case 'end':
            return _context6.stop();
        }
      }
    }, _callee6, _this);
  }));

  return function (_x8) {
    return _ref7.apply(this, arguments);
  };
}());

router.del('/note/:id', function (ctx) {
  var id = ctx.params.id;
  var index = notes.findIndex(function (note) {
    return id === note.id;
  });
  if (index !== -1) {
    var note = notes[index];
    notes.splice(index, 1);
    lastUpdated = new Date();
    broadcast({ event: 'deleted', note: note });
  }
  ctx.response.status = 204; // no content
});

setInterval(function () {
  lastUpdated = new Date();
  lastId = '' + (parseInt(lastId) + 1);
  var note = new Note({ id: lastId, text: 'Note ' + lastId, date: lastUpdated, version: 1 });
  notes.push(note);
  console.log('\n   ' + note.text);
  broadcast({ event: 'created', note: note });
}, 15000);

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(3);


/***/ }),
/* 3 */
/***/ (function(module, exports) {

module.exports = require("regenerator-runtime");

/***/ }),
/* 4 */
/***/ (function(module, exports) {

module.exports = require("koa");

/***/ }),
/* 5 */
/***/ (function(module, exports) {

module.exports = require("http");

/***/ }),
/* 6 */
/***/ (function(module, exports) {

module.exports = require("ws");

/***/ }),
/* 7 */
/***/ (function(module, exports) {

module.exports = require("koa-router");

/***/ }),
/* 8 */
/***/ (function(module, exports) {

module.exports = require("koa-cors");

/***/ }),
/* 9 */
/***/ (function(module, exports) {

module.exports = require("koa-bodyparser");

/***/ })
/******/ ]);
//# sourceMappingURL=main.map