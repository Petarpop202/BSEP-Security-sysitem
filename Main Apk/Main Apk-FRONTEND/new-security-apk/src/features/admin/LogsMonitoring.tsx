import React, { useEffect, useState } from 'react';
import agent from '../../app/api/agent';

function LogsMonitoring() {

    
    const [logs, setLogs] = useState<String[]>([]);


    useEffect(() => {
        agent.Administrator.getAllLogs()
          .then(response => {
            setLogs(response);
          })
      }, []);
      

    return (
        <div>
            <h1>Logovi</h1>
            <ul>
                {logs.map((log, index) => (
                    <li key={index}>
                        <strong>{log}</strong>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default LogsMonitoring;
