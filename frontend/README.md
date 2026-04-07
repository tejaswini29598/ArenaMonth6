# Enterprise Platform - Frontend

Production-grade React TypeScript frontend for the Enterprise Platform.

## Tech Stack

- **Framework**: React 18
- **Language**: TypeScript
- **Build**: Vite
- **State Management**: Redux Toolkit + RTK Query
- **Styling**: Tailwind CSS + Material-UI
- **Testing**: Jest + React Testing Library
- **Linting**: ESLint + Prettier

## Prerequisites

- Node.js 18+ (18.17 LTS recommended)
- npm 9+ or yarn 3+

## Quick Start

```bash
# Install dependencies
npm install

# Start development server
npm start

# Access application
http://localhost:3000
```

## Available Scripts

```bash
# Development
npm start              # Start dev server
npm run dev           # Vite dev server

# Building
npm run build         # Production build

# Testing
npm test              # Run unit tests
npm test:coverage     # Generate coverage report

# Code Quality
npm run lint          # Run ESLint
npm run format        # Format with Prettier

# Analysis
npm run analyze       # Bundle analysis
npm run lighthouse    # Lighthouse audit
```

## Project Structure

```
src/
├── components/      # Reusable React components
│   ├── common/      # Common components
│   ├── layout/      # Layout components
│   ├── forms/       # Form components
│   └── ui/          # UI components
├── pages/           # Page components
├── services/        # API clients
│   └── api.ts       # RTK Query setup
├── store/           # Redux state
│   ├── slices/      # Redux slices
│   └── store.ts     # Store configuration
├── hooks/           # Custom React hooks
├── utils/           # Utility functions
├── types/           # TypeScript types
├── styles/          # Global styles
├── App.tsx          # Root component
└── main.tsx         # Entry point
public/             # Static assets
```

## Features

- ✅ Responsive design (mobile, tablet, desktop)
- ✅ Dark mode support
- ✅ Accessibility (WCAG 2.1 AA)
- ✅ Progressive Web App (PWA)
- ✅ Offline support
- ✅ Real-time updates (WebSocket)
- ✅ Authentication/Authorization
- ✅ Error handling & toast notifications
- ✅ Loading states & skeletons
- ✅ Form validation & submission

## Development Environment Setup

### 1. Install Dependencies

```bash
npm install

# Or use yarn
yarn install
```

### 2. Configure Environment

```bash
# Copy environment template
cp .env.example .env.local

# Update with your values
# VITE_API_URL=http://localhost:8080
```

### 3. Start Development Server

```bash
npm start

# Application will open at http://localhost:3000
```

## API Integration

### Using RTK Query

```typescript
// services/api.ts
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const api = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({ 
    baseUrl: process.env.VITE_API_URL 
  }),
  endpoints: (builder) => ({
    getUsers: builder.query({
      query: () => '/users'
    }),
    createUser: builder.mutation({
      query: (user) => ({
        url: '/users',
        method: 'POST',
        body: user
      })
    })
  })
});

export const { useGetUsersQuery, useCreateUserMutation } = api;
```

### Using in Components

```typescript
// pages/Users.tsx
import { useGetUsersQuery } from '../services/api';

export const UsersList = () => {
  const { data: users, isLoading, error } = useGetUsersQuery();
  
  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error loading users</div>;
  
  return (
    <ul>
      {users?.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
};
```

## Authentication

### Setup

```typescript
// hooks/useAuth.ts
import { useSelector, useDispatch } from 'react-redux';
import { useLoginMutation, useLogoutMutation } from '../services/api';

export const useAuth = () => {
  const dispatch = useDispatch();
  const user = useSelector(state => state.auth.user);
  const [login] = useLoginMutation();
  const [logout] = useLogoutMutation();
  
  const handleLogin = async (email, password) => {
    const result = await login({ email, password }).unwrap();
    // Store token, update state
  };
  
  const handleLogout = () => {
    logout();
    // Clear token, update state
  };
  
  return { user, login: handleLogin, logout: handleLogout };
};
```

### Protected Routes

```typescript
// components/ProtectedRoute.tsx
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export const ProtectedRoute = ({ children }) => {
  const { user } = useAuth();
  
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
};
```

## Styling

### Tailwind CSS

```typescript
// Using Tailwind classes
<div className="bg-blue-500 text-white p-4 rounded-lg">
  Styled with Tailwind
</div>
```

### Material-UI Integration

```typescript
import { Button, Card, Typography } from '@mui/material';

export const MyComponent = () => (
  <Card>
    <Typography variant="h5">Title</Typography>
    <Button variant="contained">Click me</Button>
  </Card>
);
```

## Testing

### Unit Tests

```typescript
// components/__tests__/Button.test.tsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Button } from '../Button';

describe('Button Component', () => {
  it('renders button with text', () => {
    render(<Button>Click me</Button>);
    expect(screen.getByText('Click me')).toBeInTheDocument();
  });
  
  it('calls onClick when clicked', async () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick}>Click</Button>);
    
    await userEvent.click(screen.getByText('Click'));
    expect(handleClick).toHaveBeenCalled();
  });
});
```

### Component Tests with RTK Query

```typescript
// pages/__tests__/UsersList.test.tsx
import { Provider } from 'react-redux';
import { render, screen } from '@testing-library/react';
import { setupServer } from 'msw/node';
import { rest } from 'msw';
import { store } from '../../store/store';
import { UsersList } from '../UsersList';

const server = setupServer(
  rest.get('/api/users', (req, res, ctx) => {
    return res(ctx.json([
      { id: 1, name: 'User 1' },
      { id: 2, name: 'User 2' }
    ]));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test('renders users list', async () => {
  render(
    <Provider store={store}>
      <UsersList />
    </Provider>
  );
  
  expect(await screen.findByText('User 1')).toBeInTheDocument();
  expect(screen.getByText('User 2')).toBeInTheDocument();
});
```

## Build & Deployment

### Production Build

```bash
# Create optimized build
npm run build

# Preview build locally
npm run preview
```

### Docker Deployment

```bash
# Build image
docker build -t enterprise-platform-frontend .

# Run container
docker run -p 3000:3000 enterprise-platform-frontend
```

### Environment Variables

Create `.env.production` for production builds:

```
VITE_API_URL=https://api.enterprise-platform.com
VITE_APP_VERSION=2.0.0
```

## Performance Optimization

### Code Splitting

```typescript
import { lazy, Suspense } from 'react';

const HeavyComponent = lazy(() => import('./HeavyComponent'));

export const App = () => (
  <Suspense fallback={<div>Loading...</div>}>
    <HeavyComponent />
  </Suspense>
);
```

### Image Optimization

```typescript
import { Picture } from 'react-picture';

<Picture
  srcset={[
    { src: '/image-768.webp', media: '(max-width: 768px)' },
    { src: '/image-1024.webp', media: '(max-width: 1024px)' },
    { src: '/image-full.webp' }
  ]}
/>
```

### Lighthouse Performance Tips

- Tree-shake unused code
- Minimize bundle size
- Code splitting for routes
- Image optimization
- CSS minification
- Lazy load components
- Optimize fonts

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Accessibility

- ARIA labels
- Keyboard navigation
- Color contrast
- Focus management
- Semantic HTML
- Screen reader support

## Contributing

See [CONTRIBUTING.md](../CONTRIBUTING.md) for development guidelines.

## Troubleshooting

### Port Already in Use

```bash
# Change port
npm start -- --port 3001
```

### Module Not Found

```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Build Optimization

```bash
# Analyze bundle
npm run analyze

# Check for unused packages
npm audit
npx depcheck
```

## Resources

- [React Docs](https://react.dev)
- [Vite Docs](https://vitejs.dev)
- [Redux Docs](https://redux.js.org)
- [Tailwind CSS](https://tailwindcss.com)
- [Testing Library](https://testing-library.com)

## Support

- Docs: [../docs](../docs)
- Issues: GitHub Issues
- Chat: GitHub Discussions

## License

See [LICENSE](../LICENSE)
