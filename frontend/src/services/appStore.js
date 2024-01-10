import { configureStore, createSlice } from '@reduxjs/toolkit';

const appSlice = createSlice({
  name: 'appStore',
  initialState: {
    navbar: undefined,
    user: undefined,
  },
  reducers: {
    setNavbar: (state, action) => {
      state.navbar = action.payload;
    },
    setUser: (state, action) => {
      state.user = action.payload;
    }
  }
});

export const appStore = configureStore({
  reducer: {
    appStore: appSlice.reducer
  }
});
