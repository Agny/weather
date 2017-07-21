import React, { Component } from 'react';
class AxisTick extends Component {
  render() {
    const {x, y, payload} = this.props;

    return (
        <g transform={`translate(${x},${y})`}>
          <text x={0} y={0} dy={26} textAnchor="end" fill="#666" transform="rotate(-35)">{payload.value}</text>
        </g>
    );
  }
}

class CustomLabel extends Component {
  render() {
    const {x, y, stroke, value} = this.props;

    return <text x={x} y={y} dy={-4} fill={stroke} fontSize={10} textAnchor="middle">{value}</text>
  }
}

class CustomTooltip extends Component {
  propTypes:{
      type: PropTypes.string,
      payload: PropTypes.array,
      label: PropTypes.string
      };

  render() {
    const { active } = this.props;
    const style = {
      margin: '0px',
      padding: '5px',
      backgroundColor: 'rgb(255, 255, 255)',
      border: '1px solid rgb(204, 204, 204)',
      whiteSpace: 'nowrap'
    };
    if (active) {
      const { payload } = this.props;
      return (
          <div className="recharts-default-tooltip" style={style}>
            <p className="recharts-tooltip-label">{`${payload[0].value}`}</p>
          </div>
      );

    }

    return null;
  }
}

class WindTooltip extends Component {
  propTypes:{
      type: PropTypes.string,
      payload: PropTypes.array,
      label: PropTypes.string
      };

  render() {
    const { active } = this.props;
    const style = {
      margin: '0px',
      padding: '2px',
      backgroundColor: 'rgb(255, 255, 255)',
      border: '1px solid rgb(204, 204, 204)',
      whiteSpace: 'nowrap'
    };
    if (active) {
      const { payload } = this.props;
      return (
          <div className="recharts-default-tooltip" style={style}>
            <p className="recharts-tooltip-label">{`${payload[0].payload.windDirection}, ${payload[0].value}`}</p>
          </div>
      );

    }

    return null;
  }
}

export {AxisTick, CustomLabel, CustomTooltip, WindTooltip};
