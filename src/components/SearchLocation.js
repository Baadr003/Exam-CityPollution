import { Box, TextField, Autocomplete } from '@mui/material';
import { styled } from '@mui/material/styles';

const StyledTextField = styled(TextField)({
  '& .MuiOutlinedInput-root': {
    '& fieldset': {
      borderColor: 'rgba(0, 255, 245, 0.3)',
      borderRadius: '10px',
    },
    '&:hover fieldset': {
      borderColor: 'rgba(0, 255, 245, 0.5)',
    },
    '&.Mui-focused fieldset': {
      borderColor: '#00fff5',
    },
  },
  '& .MuiInputLabel-root': {
    color: 'rgba(255, 255, 255, 0.7)',
  },
  '& .MuiInputBase-input': {
    color: '#fff',
  },
});

const SearchLocation = ({ onLocationSelect }) => {
  return (
    <Box sx={{
      position: 'absolute',
      top: 20,
      left: '50%',
      transform: 'translateX(-50%)',
      zIndex: 1000,
      width: '90%',
      maxWidth: 500,
    }}>
      <Autocomplete
        freeSolo
        options={[]}
        renderInput={(params) => (
          <StyledTextField
            {...params}
            label="Rechercher une ville"
            variant="outlined"
            fullWidth
            sx={{
              backdropFilter: 'blur(10px)',
              backgroundColor: 'rgba(23, 42, 69, 0.9)',
            }}
          />
        )}
      />
    </Box>
  );
};