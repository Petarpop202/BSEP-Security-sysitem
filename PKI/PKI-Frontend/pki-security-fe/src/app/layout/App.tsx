import { Container, CssBaseline, ThemeProvider, createTheme } from "@mui/material";
import CreateCertificate from "../../features/create-certificate/CreateCertificate";
import { useState } from "react";
import { ToastContainer } from "react-toastify";

function App() {

  const [darkMode, setDarkMode] = useState(false);
  const palleteType = darkMode ? 'dark' : 'light';
  const theme = createTheme({
    palette: {
      mode: palleteType,
      background: {
        default: palleteType === 'light' ? '#eaeaea' : '#121212'
      }
    }
  })

  function handleThemeChange() {
    setDarkMode(!darkMode);
  }

  return (
    <ThemeProvider theme={theme}>
      <ToastContainer position="bottom-right" hideProgressBar theme="colored" />
      <CssBaseline />
      <Container>
        <CreateCertificate></CreateCertificate>
      </Container>
    </ThemeProvider>
  );
}

export default App;
