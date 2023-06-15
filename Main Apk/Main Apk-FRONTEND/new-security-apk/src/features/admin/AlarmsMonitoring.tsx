import React, { useEffect, useState } from 'react';
import agent from '../../app/api/agent';
import { Alarm } from '../../app/models/Alarm';

function AlarmsMonitoring() {

    
    const [alarms, setLogs] = useState<Alarm[]>([]);


    useEffect(() => {
        agent.Administrator.getAllAlarms()
          .then(response => {
            setLogs(response);
          })
      }, []);
      

    return (
        <div>
            <h1>Alarmi</h1>
            <ul>
                {alarms.map((alarm, index) => (
                    <li key={index} style={{ color: 'red' }}>
                        <strong>{alarm.message}</strong>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default AlarmsMonitoring;
