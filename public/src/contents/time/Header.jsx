import React from "react";
import style from "./time.css";

export default class Header extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className={style.header}>
        <div className={style.titleHeader}>
          <span />
        </div>
        <div className={style.lapHeader}>
          <span>LAP</span>
        </div>
        <div className={style.timeHeader}>
          <span>SECTOR 1</span>
        </div>
        <div className={style.timeHeader}>
          <span>SECTOR 2</span>
        </div>
        <div className={style.timeHeader}>
          <span>SECTOR 3</span>
        </div>
        <div className={style.timeHeader}>
          <span>LAP TIME</span>
        </div>
        <div className={style.deltaHeader}>
          <span>DELTA</span>
        </div>
      </div>
    );
  }
}
