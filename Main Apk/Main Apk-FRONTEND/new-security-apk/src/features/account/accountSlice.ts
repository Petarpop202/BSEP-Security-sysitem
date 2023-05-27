import { createAsyncThunk, createSlice, isAnyOf } from "@reduxjs/toolkit"
import { FieldValues } from "react-hook-form";
import agent from "../../app/api/agent";
import { router } from "../../app/router/Router";
import { toast } from "react-toastify";
import { User } from "../../app/models/User";
import jwt from 'jsonwebtoken';

interface AccountState {
    user: User | null;
}

const initialState: AccountState = {
    user: null
}

export const signInUser = createAsyncThunk<User,  FieldValues>(
    'account/signInUser',
    async (data, thunkAPI) => {
        try {
            const user = await agent.Account.login(data);
            const decodedToken = jwt.verify(user?.jwt, 'tajna_lozinka');
            const role = decodedToken.role;
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('userRole', JSON.stringify(role));
            return user;
        } catch (error: any) {
            return thunkAPI.rejectWithValue({error: error.data})
        }
    }
)
//const user = await agent.Account.currentUser();
export const fetchCurrentUser = createAsyncThunk<User>(
    'account/fetchCurrentUser',
    async (_, thunkAPI) => {
        thunkAPI.dispatch(setUser(JSON.parse(localStorage.getItem('user')!)));
        try {
            const user = await agent.Account.refresh("aasd");
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('userRole', JSON.stringify(user.userRole));
            return user;
        } catch (error: any) {
            return thunkAPI.rejectWithValue({error: error.data})
        }
    },
    {
        condition: () => {
            if (!localStorage.getItem('user')) return false;
        }
    }
)

export const accountSlice = createSlice({
    name: 'account',
    initialState,
    reducers: {
        signOut: (state) => {
            state.user = null;
            localStorage.removeItem('user');
            localStorage.removeItem('userRole');
            router.navigate('/');
        },
        setUser: (state, action) => {
            state.user = action.payload;
        }
    },
    extraReducers: (builder => {
        builder.addCase(fetchCurrentUser.rejected, (state) => {
            state.user = null;
            localStorage.removeItem('user');
            localStorage.removeItem('userRole');
            toast.error('Session expired - please login again');
            router.navigate('/');
        })
        builder.addMatcher(isAnyOf(signInUser.fulfilled, fetchCurrentUser.fulfilled), (state, action) =>{
            state.user = action.payload;
        });
        builder.addMatcher(isAnyOf(signInUser.rejected), (state, action) => {
            console.log(action.payload)
        })
    })
})

export const {signOut, setUser} = accountSlice.actions;