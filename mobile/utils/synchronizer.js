
export default class Synchronizer {
  constructor(store) {
    this.store = store;
  }

  logChwList() {
    return this.store.getState().tablesReducer.chwList;
  }

  synchronizeChws() {
    this.logChwList();
  }
}
