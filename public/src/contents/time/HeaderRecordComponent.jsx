import React from "react";
import timeStyle from "./time.css";

export default class HeaderRecordComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className={timeStyle.headerColumn}>
        <div className={timeStyle.headerCell}>
          <div>
            <span></span>
          </div>
        </div>
        <div className={timeStyle.lapCountCell}>
          <div>
            <span>LAP</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>SECTOR 1</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>SECTOR 2</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>SECTOR 3</span>
          </div>
        </div>
        <div className={timeStyle.lapTimeCell}>
          <div>
            <span>LAP TIME</span>
          </div>
        </div>
      </div>
    );
  }
}
