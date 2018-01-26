
export default class Synchronizer {
  constructor(store) {
    this.store = store;
  }

  logChwList() {
    console.log(this.store.getState().tablesReducer.chwList);
  }

  synchronizeChws() {
    this.logChwList();
  }
}
