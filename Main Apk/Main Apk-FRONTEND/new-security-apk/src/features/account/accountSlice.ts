import { createAsyncThunk, createSlice, isAnyOf } from "@reduxjs/toolkit"
import { FieldValues } from "react-hook-form"
import agent from "../../app/api/agent"
import { router } from "../../app/router/Router"
import { toast } from "react-toastify"
import { User } from "../../app/models/User"
import { decodeToken } from "react-jwt"
import { DecodedToken } from "../../app/models/DecodedToken"
import { RootState } from "../../app/apk/configureApk"

interface AccountState {
  user: User | null
}

const initialState: AccountState = {
  user: null,
}

export const signInUser = createAsyncThunk<User, FieldValues>(
  "account/signInUser",
  async (data, thunkAPI) => {
    try {
      //const jwt = await agent.Account.login(data)
      const jwt = {
        jwt: data?.jwt,
        refreshJwt: data?.refreshJwt
      }
      const decodedToken = decodeToken<DecodedToken>(jwt?.jwt)
      const user: User = decodedToken
        ? {
            id: decodedToken.id,
            name: decodedToken.name,
            username: decodedToken.sub,
            surname: decodedToken.surname,
            userRole: decodedToken.roles,
            token: jwt,
          }
        : ({} as User)
      localStorage.setItem("user", JSON.stringify(user))
      localStorage.setItem("userRole", JSON.stringify(decodedToken?.roles))
      return user
    } catch (error: any) {
      return thunkAPI.rejectWithValue({ error: error.data })
    }
  }
)

export const signInGuestUser = createAsyncThunk<User, FieldValues>(
  "account/signInGuestUser",
  async (data, thunkAPI) => {
    try {
      const jwt = {
        jwt: data.jwt,
        refreshJwt: data.refreshJwt
      }
      const decodedToken = decodeToken<DecodedToken>(jwt?.jwt)
      const user: User = decodedToken
        ? {
            id: decodedToken.id,
            name: decodedToken.name,
            username: decodedToken.sub,
            surname: decodedToken.surname,
            userRole: decodedToken.roles,
            token: jwt,
          }
        : ({} as User)
      localStorage.setItem("user", JSON.stringify(user))
      localStorage.setItem("userRole", JSON.stringify(decodedToken?.roles))
      return user
    } catch (error: any) {
      return thunkAPI.rejectWithValue({ error: error.data })
    }
  }
)

export const refreshUser = createAsyncThunk<User, FieldValues>(
  "account/refreshUser",
  async (data, thunkAPI) => {
    try {
      const jwt = await agent.Account.refresh(data)
      const decodedToken = decodeToken<DecodedToken>(jwt?.jwt)
      localStorage.removeItem("user")
      localStorage.removeItem("userRole")
      const user: User = decodedToken
        ? {
            id: decodedToken.id,
            name: decodedToken.name,
            username: decodedToken.sub,
            surname: decodedToken.surname,
            userRole: decodedToken.roles,
            token: jwt,
          }
        : ({} as User)
      localStorage.setItem("user", JSON.stringify(user))
      localStorage.setItem("userRole", JSON.stringify(decodedToken?.roles))

      return user
    } catch (error: any) {
      return thunkAPI.rejectWithValue({ error: error.data })
    }
  }
)

export const isLoggedUser = createAsyncThunk<User>(
  "account/isLoggedUser",
  async (_, thunkAPI) => {
    try {
      const userString = localStorage.getItem("user")
      let user1 = null
      if (userString !== null) {
        try {
          user1 = JSON.parse(userString)
        } catch (error) {
          console.error("Greška pri parsiranju JSON-a:", error)
        }
      }

      return user1
    } catch (error: any) {
      return thunkAPI.rejectWithValue({ error: error.data })
    }
  }
)

//const user = await agent.Account.currentUser();
export const fetchCurrentUser = createAsyncThunk<User>(
  "account/fetchCurrentUser",
  async (_, thunkAPI) => {
    thunkAPI.dispatch(setUser(JSON.parse(localStorage.getItem("user")!)))
    try {
      const user = await agent.Account.refresh("aasd")
      localStorage.setItem("user", JSON.stringify(user))
      localStorage.setItem("userRole", JSON.stringify(user.userRole))
      return user
    } catch (error: any) {
      return thunkAPI.rejectWithValue({ error: error.data })
    }
  },
  {
    condition: () => {
      if (!localStorage.getItem("user")) return false
    },
  }
)

export const accountSlice = createSlice({
  name: "account",
  initialState,
  reducers: {
    signOut: (state) => {
      state.user = null
      localStorage.removeItem("user")
      localStorage.removeItem("userRole")
      router.navigate("/")
    },
    setUser: (state, action) => {
      state.user = action.payload
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchCurrentUser.rejected, (state) => {
      state.user = null
      localStorage.removeItem("user")
      localStorage.removeItem("userRole")
      toast.error("Session expired - please login again")
      router.navigate("/")
    })
    builder.addCase(refreshUser.fulfilled, (state, action) => {
      state.user = action.payload
    })
    builder.addMatcher(
      isAnyOf(
        signInUser.fulfilled,
        fetchCurrentUser.fulfilled,
        isLoggedUser.fulfilled,
        signInGuestUser.fulfilled
      ),
      (state, action) => {
        state.user = action.payload
      }
    )
    builder.addMatcher(isAnyOf(signInUser.rejected), (state, action) => {
      console.log(action.payload)
    })
  },
})

export const { signOut, setUser } = accountSlice.actions
